package hr.fer.zemris.java.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * {@code SmartHttpServer} is a HTTP server.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class SmartHttpServer {

    /** Source directory of workers. */
    private static Path WORKERS_SOURCE = Paths.get("src/hr/fer/zemris/java/webserver/workers");

    /** Address of the server. */
    private String address;
    /** Port of the server. */
    private int port;
    /** Number of worker threads. */
    private int workerThreads;
    /** Number of seconds until session timeout. */
    private int sessionTimeout;
    /** Map of all mime types. */
    private Map<String, String> mimeTypes = new HashMap<>();
    /** Map of all workers. */
    private Map<String, IWebWorker> workersMap = new HashMap<>();
    /** Server thread that calls {@code ClientWorker} upon request. */
    private ServerThread serverThread;
    /** Thread pool for workers. */
    private ExecutorService threadPool;
    /** Document root directory. */
    private Path documentRoot;
    /** Map of all sessions. */
    private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
    /** Session random number generator. */
    private Random sessionRandom = new Random();

    /**
     * Constructs a new {@code SmartHttpServer} with specified configuration
     * file name.
     * 
     * @param configFileName
     *            configuration file name
     */
    public SmartHttpServer(String configFileName) {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(configFileName)));
        } catch (IOException ignorable) {
        }

        address = properties.getProperty("server.address");
        port = Integer.valueOf(properties.getProperty("server.port"));
        workerThreads = Integer.valueOf(properties.getProperty("server.workerThreads"));
        sessionTimeout = Integer.valueOf(properties.getProperty("session.timeout"));
        documentRoot = Paths.get(properties.getProperty("server.documentRoot"));

        loadMimeTypes(Paths.get(properties.getProperty("server.mimeConfig")));
        loadWebWorkers(Paths.get(properties.getProperty("server.workers")));

        serverThread = new ServerThread();
    }

    /**
     * Starts the server.
     */
    protected synchronized void start() {
        if (threadPool != null)
            return;

        serverThread = new ServerThread();
        serverThread.start();

        threadPool = Executors.newFixedThreadPool(workerThreads, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Stops the server.
     */
    protected synchronized void stop() {
        if (threadPool == null)
            return;

        serverThread.stopServer();
        serverThread = null;
        threadPool.shutdown();
        threadPool = null;
    }

    /**
     * Loads the mime types.
     * 
     * @param mimePath
     *            the path to mime configuration file
     */
    private void loadMimeTypes(Path mimePath) {
        Properties properties = new Properties();

        try {
            properties.load(Files.newInputStream(mimePath));
        } catch (IOException ignorable) {
        }

        properties.keySet().forEach(key -> mimeTypes.put((String) key, properties.getProperty((String) key)));
    }

    /**
     * Loads the web workers from {@value #WORKERS_SOURCE} path and those
     * specified by the configuration file.
     * 
     * @param workerPath
     *            the path to workers configuration file
     */
    private void loadWebWorkers(Path workerPath) {
        Properties properties = new Properties();

        try {
            properties.load(Files.newInputStream(workerPath));
        } catch (IOException ignorable) {
        }

        properties.keySet().forEach(key -> {
            try {
                Class<?> clazz = Class.forName(properties.getProperty((String) key));
                workersMap.put((String) key, (IWebWorker) clazz.newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignorable) {
            }
        });

        loadWebWorkers();
    }

    /**
     * Loads the web workers from {@value #WORKERS_SOURCE} path.
     */
    private void loadWebWorkers() {
        try {
            Files.walk(WORKERS_SOURCE).filter(p -> Files.isRegularFile(p))
                    .forEach(path -> {
                        try {
                            String key = path.toString().replace(File.separator, ".");
                            key = key.substring(4, key.lastIndexOf('.'));

                            Class<?> clazz = Class.forName(key);
                            workersMap.put("/ext/" + clazz.getSimpleName(), (IWebWorker) clazz.newInstance());
                        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignorable) {
                        }
                    });
        } catch (IOException ignorable) {
        }
    }

    /**
     * {@code ServerThread} is a main server thread that submits the
     * {@code ClientWorker} upon request.
     * 
     * @author Karlo Vrbić
     * @version 1.0
     * @see Thread
     */
    protected class ServerThread extends Thread {

        /** Indicates whether this thread is stopped from accepting requests. */
        private boolean stop;

        @Override
        public void run() {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket();

                serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
            } catch (IOException e) {
            }

            while (!stop) {
                Socket client = null;
                try {
                    client = serverSocket.accept();
                } catch (IOException e) {
                    continue;
                }
                ClientWorker cw = new ClientWorker(client);
                threadPool.submit(cw);
            }
        }

        /**
         * Stops the server thread.
         */
        public void stopServer() {
            stop = true;
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@code ClientWorker} is a class handles client requests,
     * 
     * @author Karlo Vrbić
     * @version 1.0
     * @see Runnable
     */
    private class ClientWorker implements Runnable {

        /** Client socket. */
        private Socket csocket;
        /** Input stream. */
        private PushbackInputStream istream;
        /** Output stream. */
        private OutputStream ostream;
        /** Version of the HTTP. */
        private String version;
        /** Method of the request. */
        private String method;
        /** Map of all parameters. */
        private Map<String, String> params = new LinkedHashMap<String, String>();
        /** Map of all persistent parameters. */
        private Map<String, String> permParams = new LinkedHashMap<String, String>();
        /** Map of all output cookies. */
        private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
        /** Session ID. */
        private String SID;

        /**
         * Constructs a new {@code ClientWorker} with specified client socket.
         * 
         * @param csocket
         *            client socket
         */
        public ClientWorker(Socket csocket) {
            super();
            this.csocket = csocket;
        }

        @Override
        public void run() {
            try {
                istream = new PushbackInputStream(csocket.getInputStream());
                ostream = csocket.getOutputStream();
            } catch (IOException ignorable) {
            }

            List<String> request = readRequest();

            if (request.size() < 1) {
                sendError(ostream, 400, "Bad Request");
                return;
            }

            String firstLine = request.get(0);

            String[] parts = firstLine.split(" ");

            method = parts[0];
            String requestedPath = parts[1].substring(1);
            version = parts[2];

            if (!method.equals("GET")) {
                sendError(ostream, 400, "Bad Request");
                return;
            }

            if (!(version.equals("HTTP/1.0") || version.equals("HTTP/1.1"))) {
                sendError(ostream, 400, "Bad Request");
                return;
            }

            processSession(request);

            String path = null;
            String paramString = null;

            if (requestedPath.contains("?")) {
                String[] pathAndParams = requestedPath.split("\\?");

                path = pathAndParams[0];
                paramString = pathAndParams[1];

                parseParameters(paramString);
            } else {
                path = requestedPath;
            }

            Path reqPath = documentRoot.resolve(path);

            if (reqPath.toString().length() < documentRoot.toString().length()) {
                sendError(ostream, 403, "Forbidden");
                return;
            }
            IWebWorker worker = null;

            if (path.startsWith("ext/") || workersMap.containsKey("/" + path)) {
                worker = workersMap.get("/" + path);
            } else if (!(Files.exists(reqPath) && Files.isReadable(reqPath))) {
                sendError(ostream, 404, "Not Found");
                return;
            }

            String extension = path.substring(requestedPath.lastIndexOf('.') + 1);

            String mimeType = mimeTypes.get(extension);

            RequestContext rc = new RequestContext(ostream, params, permParams, outputCookies);

            rc.setMimeType(mimeType);
            rc.setStatusCode(200);
            rc.setStatusText("OK");

            if (worker != null) {
                worker.processRequest(rc);
            } else if (extension.equals("smscr")) {
                getSmartScriptEngine(reqPath, rc).execute();
            } else {
                try {
                    byte[] bytes = Files.readAllBytes(reqPath);

                    rc.write(bytes);
                } catch (IOException e) {
                }
            }

            try {
                csocket.close();
            } catch (IOException ignorable) {
            }
        }

        /**
         * Processes the session.
         * 
         * @param request
         *            list of lines of a HTTP request
         */
        private synchronized void processSession(List<String> request) {
            String sidCandidate = checkSession(request);

            if (sidCandidate == null) {
                SessionMapEntry entry = generateSessionEntry();

                sessions.put(entry.sid, entry);
                outputCookies.add(new RequestContext.RCCookie("sid", entry.sid, null, address, "/", true));
            } else {
                SessionMapEntry entry = sessions.get(sidCandidate);

                if (entry == null || entry.validUntil < System.currentTimeMillis()) {
                    SessionMapEntry newEntry = generateSessionEntry(sidCandidate);

                    sessions.put(newEntry.sid, newEntry);
                    outputCookies.add(new RequestContext.RCCookie("sid", newEntry.sid, null, address, "/", true));
                } else {
                    SID = entry.sid;

                    entry.validUntil = System.currentTimeMillis() + sessionTimeout * 1000;
                    permParams = entry.map;
                }
            }
        }

        /**
         * Checks if there is a cookie with a sid parameter and returns it if it
         * exists.
         * 
         * @param request
         *            list of lines of a HTTP request
         * @return sid parameter if exists; {@code null} otherwise
         */
        private String checkSession(List<String> request) {
            String sidLine = request.stream()
                    .filter(x -> x.matches("Cookie: sid=([A-Z]*)"))
                    .findFirst().orElse(null);

            String sidCandidate = null;
            if (sidLine != null) {
                sidCandidate = sidLine.substring(sidLine.indexOf('=') + 1);
            }

            return sidCandidate;
        }

        /**
         * Generate random session ID and returns it.
         * 
         * @return random session ID
         */
        private String generateSID() {
            StringBuilder sb = new StringBuilder();

            sessionRandom.ints(65, 90)
                    .limit(20)
                    .forEach(c -> sb.append((char) c));

            return sb.toString();
        }

        /**
         * Generates the session entry with specified sid and returns it.
         * 
         * @param sid
         *            session ID
         * @return session entry
         */
        private SessionMapEntry generateSessionEntry(String sid) {
            Objects.requireNonNull(sid, "You cannot generate session entry with a null reference as a SID!");

            SessionMapEntry entry = new SessionMapEntry();
            entry.sid = sid;
            entry.map = permParams;
            entry.validUntil = System.currentTimeMillis() + sessionTimeout * 1000;

            SID = entry.sid;

            return entry;
        }

        /**
         * Generates the session entry with random sid.
         * 
         * @return session entry
         */
        private SessionMapEntry generateSessionEntry() {
            return generateSessionEntry(generateSID());
        }

        /**
         * Parses parameters given trough HTTP request.
         * 
         * @param paramString
         *            parameter string
         */
        private void parseParameters(String paramString) {
            if (paramString == null)
                return;

            String[] paramPairs = paramString.split("&");

            for (String param : paramPairs) {
                String[] splitedParam = param.split("=");

                params.put(splitedParam[0], splitedParam[1]);
            }
        }

        /**
         * Reads HTTP request and returns the list of all lines.
         * 
         * @return the list of all lines from HTTP request
         */
        private List<String> readRequest() {
            byte[] request = null;

            try {
                request = readRequest(istream);
            } catch (IOException e) {
            }

            if (request == null) {
                sendError(ostream, 400, "Bad request");
                return null;
            }

            String requestStr = new String(request, StandardCharsets.US_ASCII);

            List<String> headers = new ArrayList<String>();
            String currentLine = null;

            for (String s : requestStr.split("\n")) {
                if (s.isEmpty())
                    break;
                char c = s.charAt(0);
                if (c == 9 || c == 32) {
                    currentLine += s;
                } else {
                    if (currentLine != null) {
                        headers.add(currentLine);
                    }
                    currentLine = s;
                }
            }
            if (!currentLine.isEmpty()) {
                headers.add(currentLine);
            }
            return headers;
        }

        /**
         * Reads HTTP request.
         * 
         * @param is
         *            input stream
         * @return bytes representation of a request
         * @throws IOException
         *             if an I/O error occurs
         */
        private byte[] readRequest(InputStream is) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int state = 0;
            l: while (true) {
                int b = is.read();
                if (b == -1)
                    return null;
                if (b != 13) {
                    bos.write(b);
                }
                switch (state) {
                    case 0:
                        if (b == 13) {
                            state = 1;
                        } else if (b == 10)
                            state = 4;
                        break;
                    case 1:
                        if (b == 10) {
                            state = 2;
                        } else
                            state = 0;
                        break;
                    case 2:
                        if (b == 13) {
                            state = 3;
                        } else
                            state = 0;
                        break;
                    case 3:
                        if (b == 10) {
                            break l;
                        } else
                            state = 0;
                        break;
                    case 4:
                        if (b == 10) {
                            break l;
                        } else
                            state = 0;
                        break;
                }
            }
            return bos.toByteArray();
        }

        /**
         * Sends the error response on specified output stream and with
         * specified status code and status text.
         * 
         * @param cos
         *            client output stream
         * @param statusCode
         *            HTTP status code
         * @param statusText
         *            status text
         */
        private void sendError(OutputStream cos, int statusCode, String statusText) {
            try {
                cos.write(
                        ("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                                "Server: simple java server\r\n" +
                                "Content-Type: text/plain;charset=UTF-8\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n").getBytes(StandardCharsets.US_ASCII));

                cos.flush();
            } catch (IOException e) {
            }
        }

        /**
         * Returns the {@linkplain SmartScriptEngine} for specified path of a
         * script and request context.
         * 
         * @param path
         *            path of a script
         * @param rc
         *            request context
         * @return {@linkplain SmartScriptEngine}
         */
        private SmartScriptEngine getSmartScriptEngine(Path path, RequestContext rc) {
            String documentBody = readFromDisk(path);

            return new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc);
        }

        /**
         * Reads a file from disk at the specified path.
         * 
         * @param path
         *            path of the file
         * @return a string of a file
         */
        private String readFromDisk(Path path) {
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                System.err.println("Path " + path.toAbsolutePath().toString() + " doesn't exists or isn't a file!");
                return null;
            }

            byte[] data = null;
            try {
                data = Files.readAllBytes(path);
            } catch (IOException e) {
                System.err.println("Some I/O error occured while reading from the file" + path.toAbsolutePath() + "!");
                return null;
            }

            return new String(data, StandardCharsets.UTF_8);
        }
    }

    /**
     * {@code SessionMapEntry} is a class that contains the information about
     * sessions.
     * 
     * @author Karlo Vrbić
     * @version 1.0
     */
    private static class SessionMapEntry {

        /** Sessio ID. */
        String sid;
        /** Indicates time until this session is valid. */
        long validUntil;
        /** Map of parameters. */
        Map<String, String> map;

    }

    /**
     * Starting point of a program.
     * 
     * @param args
     *            Command-line argument
     */
    public static void main(String[] args) {
        SmartHttpServer server = new SmartHttpServer("config/server.properties");

        server.start();
    }
}
