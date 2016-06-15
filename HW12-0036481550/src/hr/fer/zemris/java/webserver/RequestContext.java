package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * {@code RequestContext} class is used to generate HTTP requests and write them
 * to a output file.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class RequestContext {

    /** Default encoding for the output file. */
    public static final String DEFAULT_ENCODING = "UTF-8";
    /** Default status code of the HTTP request. */
    public static final int DEFAULT_STATUS_CODE = 200;
    /** Default status text of the HTTP request. */
    public static final String DEFAULT_STATUS_TEXT = "OK";
    /** Default mime type of the HTTP request. (e.g. "text/plain") */
    public static final String DEFAULT_MIME_TYPE = "text/html";

    /** Encoding for the output file. */
    public String encoding = DEFAULT_ENCODING;
    /** Status code of the HTTP request. */
    public int statusCode = DEFAULT_STATUS_CODE;
    /** Status text of the HTTP request. */
    public String statusText = DEFAULT_STATUS_TEXT;
    /** Mime type of the HTTP request. (e.g. "text/plain") */
    public String mimeType = DEFAULT_MIME_TYPE;

    /** Output stream for the output file. */
    private OutputStream outputStream;
    /** Coding page used for writing to output file. */
    private Charset charset;

    /** Map of parameters for the HTTP request. */
    private Map<String, String> parameters;
    /** Map of temporary parameters for the HTTP request. */
    private Map<String, String> temporaryParameters;
    /** Map of persistent parameters for the HTTP request. */
    private Map<String, String> persistentParameters;
    /** List of the cookies. */
    private List<RCCookie> outputCookies;

    /**
     * Flag which indicates if this class has already written something to
     * output file.
     */
    private boolean headerGenerated;

    /**
     * Constructs a new {@code RequestContext} object with specified parameters.
     * 
     * @param outputStream
     *            the output stream for the output file
     * @param parameters
     *            the parameters for the HTTP request
     * @param persistentParameters
     *            the persistent parameters for the HTTP request
     * @param outputCookies
     *            output cookies
     * @throws NullPointerException
     *             if {@code outputStream} parameter is a {@code null} reference
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters,
            Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
        this.outputStream = Objects
                .requireNonNull(outputStream, "You cannot instantiate RequestContent with null as output stream!");

        this.parameters = new LinkedHashMap<>();
        this.persistentParameters = new LinkedHashMap<>();
        this.outputCookies = new ArrayList<>();
        this.temporaryParameters = new LinkedHashMap<>();

        if (parameters != null) {
            this.parameters.putAll(parameters);
        }
        if (persistentParameters != null) {
            this.persistentParameters.putAll(persistentParameters);
        }
        if (outputCookies != null) {
            this.outputCookies.addAll(outputCookies);
        }
    }

    /**
     * Returns a parameter with specified {@code name}.
     * 
     * @param name
     *            the {@code name} of a parameter
     * @return parameter with specified {@code name}
     * @throws NullPointerException
     *             if {@code name} parameter is a {@code null} reference
     */
    public String getParameter(String name) {
        Objects.requireNonNull(name, "You cannot use null reference as a key in parameters map!");

        return parameters.get(name);
    }

    /**
     * Returns the unmodifiable set of all parameter names for the HTTP request.
     * 
     * @return the unmodifiable set of all parameter names for the HTTP request
     */
    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    /**
     * Returns a persistent parameter with specified {@code name}.
     * 
     * @param name
     *            the {@code name} of a persistent parameter
     * @return persistent parameter with specified {@code name}
     * @throws NullPointerException
     *             if {@code name} parameter is a {@code null} reference
     */
    public String getPersistentParameter(String name) {
        Objects.requireNonNull(name, "You cannot use null reference as a key in persistent parameters map!");

        return persistentParameters.get(name);
    }

    /**
     * Returns the unmodifiable set of all persistent parameter names for the
     * HTTP request.
     * 
     * @return the unmodifiable set of all persistent parameter names for the
     *         HTTP request
     */
    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    /**
     * Associates the specified {@code value} with the specified {@code name} in
     * this map of persistent parameters. If the map previously contained a
     * mapping for the {@code name}, the old {@code value} is replaced by the
     * specified {@code value}.
     * 
     * @param name
     *            the name with which the specified value is to be associated
     * @param value
     *            the value to be associated with the specified key
     * @throws NullPointerException
     *             if {@code name} or {@code value} parameters are a
     *             {@code null} references
     */
    public void setPersistentParameter(String name, String value) {
        Objects.requireNonNull(name, "You cannot use null reference as a key in persistent parameters map!");
        Objects.requireNonNull(value, "You cannot use null reference as a value in persistent parameters map!");

        persistentParameters.put(name, value);
    }

    /**
     * Removes the mapping for a {@code name} from persistent parameters if it
     * is present.
     * 
     * @param name
     *            the {@code name} of a persistent parameter
     * @throws NullPointerException
     *             if {@code name} parameter is a {@code null} reference
     */
    public void removePersistentParameter(String name) {
        Objects.requireNonNull(name, "You cannot use null reference as a key in persistent parameters map!");

        persistentParameters.remove(name);
    }

    /**
     * Returns a temporary parameter with specified {@code name}.
     * 
     * @param name
     *            the {@code name} of a temporary parameter
     * @return temporary parameter with specified {@code name}
     * @throws NullPointerException
     *             if {@code name} parameter is a {@code null} reference
     */
    public String getTemporaryParameter(String name) {
        Objects.requireNonNull(name, "You cannot use null reference as a key in temporary parameters map!");

        return temporaryParameters.get(name);
    }

    /**
     * Returns the unmodifiable set of all temporary parameter names for the
     * HTTP request.
     * 
     * @return the unmodifiable set of all temporary parameter names for the
     *         HTTP request
     */
    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    /**
     * Associates the specified {@code value} with the specified {@code name} in
     * this map of temporary parameters. If the map previously contained a
     * mapping for the {@code name}, the old {@code value} is replaced by the
     * specified {@code value}.
     * 
     * @param name
     *            the name with which the specified value is to be associated
     * @param value
     *            the value to be associated with the specified key
     * @throws NullPointerException
     *             if {@code name} or {@code value} parameters are a
     *             {@code null} references
     */
    public void setTemporaryParameter(String name, String value) {
        Objects.requireNonNull(name, "You cannot use null reference as a key in temporary parameters map!");
        Objects.requireNonNull(value, "You cannot use null reference as a value in temporary parameters map!");

        temporaryParameters.put(name, value);
    }

    /**
     * Removes the mapping for a {@code name} from temporary parameters if it is
     * present.
     * 
     * @param name
     *            the {@code name} of a temporary parameter
     * @throws NullPointerException
     *             if {@code name} parameter is a {@code null} reference
     */
    public void removeTemporaryParameter(String name) {
        Objects.requireNonNull(name, "You cannot use null reference as a key in temporary parameters map!");

        temporaryParameters.remove(name);
    }

    /**
     * Appends the specified {@code cookie} to the output cookies.
     * 
     * @param cookie
     *            the cookie of this HTTP request
     */
    public void addRCCookie(RCCookie cookie) {
        Objects.requireNonNull(cookie, "You cannot add a null reference as a cookie in output cookies list!");

        outputCookies.add(cookie);
    }

    /**
     * Writes bytes from the {@code data} array to the output file.
     * 
     * @param data
     *            data to be written to a output file
     * @return this
     * @throws IOException
     *             if an I/O error occurs
     * @throws NullPointerException
     *             if {@code data} parameter is a {@code null} reference
     */
    public RequestContext write(byte[] data) throws IOException {
        Objects.requireNonNull(data, "You cannot write null as data to output stream!");

        if (!headerGenerated) {
            outputStream.write(getHeader());
            headerGenerated = true;
            charset = Charset.forName(encoding);
        }

        outputStream.write(data);
        outputStream.flush();

        return this;
    }

    /**
     * Writes {@code data} array to the output file.
     * 
     * @param text
     *            text to be written to a output file
     * @return this
     * @throws IOException
     *             if an I/O error occurs
     * @throws NullPointerException
     *             if {@code text} parameter is a {@code null} reference
     */
    public RequestContext write(String text) throws IOException {
        Objects.requireNonNull(text, "You cannot write null as data to output stream!");

        if (!headerGenerated) {
            outputStream.write(getHeader());
            headerGenerated = true;
            charset = Charset.forName(encoding);
        }

        return write(text.getBytes(charset));
    }

    /**
     * Sets the new encoding.
     * 
     * @param encoding
     *            the new encoding
     */
    public void setEncoding(String encoding) {
        if (headerGenerated)
            throw new RuntimeException("You cannot change encoding once the header is obtained!");

        this.encoding = encoding;
    }

    /**
     * Sets the new status code.
     * 
     * @param statusCode
     *            the new status code
     */
    public void setStatusCode(int statusCode) {
        if (headerGenerated)
            throw new RuntimeException("You cannot change status code once the header is obtained!");

        this.statusCode = statusCode;
    }

    /**
     * Sets the new status text.
     * 
     * @param statusText
     *            the new status text
     */
    public void setStatusText(String statusText) {
        if (headerGenerated)
            throw new RuntimeException("You cannot change status text once the header is obtained!");

        this.statusText = statusText;
    }

    /**
     * Sets the new mime type.
     * 
     * @param mimeType
     *            the new mime type
     */
    public void setMimeType(String mimeType) {
        if (headerGenerated)
            throw new RuntimeException("You cannot change status mime type the header is obtained!");

        if (mimeType == null)
            return;

        this.mimeType = mimeType;
    }

    /**
     * Returns the byte array containing HTML request header with provided
     * parameters and encoded with {@link StandardCharsets#ISO_8859_1} encoding.
     *
     * @return the byte array containing HTML request header
     */
    private byte[] getHeader() {
        StringBuilder sb = new StringBuilder();
        final String newLine = "\r\n";

        // 1st line
        sb.append("HTTP/1.1 " + statusCode + " " + statusText + newLine);

        // 2nd line
        sb.append("Content-Type: " + mimeType);
        if (mimeType.startsWith("text/")) {
            sb.append("; charset=" + encoding);
        }
        sb.append(newLine);

        for (RCCookie cookie : outputCookies) {
            sb.append("Set-Cookie: " + cookie.name + "=" + cookie.value);

            if (cookie.domain != null) {
                sb.append("; Domain=" + cookie.domain);
            }
            if (cookie.path != null) {
                sb.append("; Path=" + cookie.path);
            }
            if (cookie.maxAge != null) {
                sb.append("; Max-Age=" + cookie.maxAge);
            }
            if(cookie.httpOnly == true) {
                sb.append("; HttpOnly");
            }

            sb.append(newLine);
        }

        sb.append(newLine);

        return sb.toString().getBytes(StandardCharsets.ISO_8859_1);
    }

    /**
     * {@code RCCookie} class represents an HTTP cookie.
     * 
     * @author Karlo Vrbić
     * @version 1.0
     */
    public static class RCCookie {

        /** Name of the cookie. */
        private String name;
        /** Value associated wit the name. */
        private String value;
        /** Number of seconds this cookie will live. */
        private Integer maxAge;
        /** Domain user requested. */
        private String domain;
        /** Path user requested. */
        private String path;
        /**
         * Indicates if this cookie will be visible only to HTTP/HTTPS requests.
         */
        private boolean httpOnly;

        /**
         * Constructs a new {@code RCCookie} object with specified parameters.
         * 
         * @param name
         *            name of the cookie
         * @param value
         *            value associated wit the name
         * @param maxAge
         *            number of seconds this cookie will live
         * @param domain
         *            domain user requested
         * @param path
         *            path user requested
         * @param httpOnly
         *            indicates if this cookie will be visible only to
         *            HTTP/HTTPS requests
         */
        public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean httpOnly) {
            this.name = Objects.requireNonNull(name, "You cannot in instantiate RCCookie with null as a name!");
            this.value = Objects.requireNonNull(value, "You cannot in instantiate RCCookie with null as a value!");
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
            this.httpOnly = httpOnly;
        }

        /**
         * Returns the name of the cookie.
         * 
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the value associated wit the name.
         * 
         * @return the value associated wit the name
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns the number of seconds this cookie will live.
         * 
         * @return the number of seconds this cookie will live
         */
        public Integer getMaxAge() {
            return maxAge;
        }

        /**
         * Returns the domain user requested.
         * 
         * @return the domain user requested
         */
        public String getDomain() {
            return domain;
        }

        /**
         * Returns the path user requested.
         * 
         * @return the path user requested
         */
        public String getPath() {
            return path;
        }

        /**
         * Checks if this cookie will be visible only to HTTP/HTTPS requests.
         * 
         * @return {@code true} if this cookie will be visible only to
         *         HTTP/HTTPS requests; {@code false} otherwise
         */
        public boolean isHttpOnly() {
            return httpOnly;
        }

    }

}