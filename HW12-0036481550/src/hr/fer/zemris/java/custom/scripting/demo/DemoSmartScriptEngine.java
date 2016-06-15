package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * {@code DemoSmartScriptEngine} is demonstrative class that demonstrates the
 * use and features of the {@link SmartScriptEngine} class.
 * <p>
 * Command-line arguments aren't used.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see SmartScriptEngine
 */
public class DemoSmartScriptEngine {

    /**
     * Starting point of a program.
     * 
     * @param args
     *            Command-line argument
     * @throws IOException
     *             if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        demo1();
    }

    /**
     * Demonstrates the use of a {@code SmartScriptEngine} class.
     */
    private static void demo1() {
        String documentBody = readFromDisk("webroot/scripts/osnovni.smscr");

        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String> persistentParameters = new HashMap<String, String>();
        List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();

    }

    /**
     * Demonstrates the use of a {@code SmartScriptEngine} class.
     */
    @SuppressWarnings("unused")
    private static void demo2() {
        String documentBody = readFromDisk("webroot/scripts/zbrajanje.smscr");
        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String> persistentParameters = new HashMap<String, String>();
        List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
        parameters.put("a", "4");
        parameters.put("b", "2");

        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();

    }

    /**
     * Demonstrates the use of a {@code SmartScriptEngine} class.
     */
    @SuppressWarnings("unused")
    private static void demo3() {
        String documentBody = readFromDisk("webroot/scripts/brojPoziva.smscr");
        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String> persistentParameters = new HashMap<String, String>();
        List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
        persistentParameters.put("brojPoziva", "3");
        RequestContext rc = new RequestContext(System.out, parameters, persistentParameters,
                cookies);
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
        System.out.println("\nVrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));

    }

    /**
     * Demonstrates the use of a {@code SmartScriptEngine} class.
     */
    @SuppressWarnings("unused")
    private static void demo4() {
        String documentBody = readFromDisk("webroot/scripts/fibonacci.smscr");
        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String> persistentParameters = new HashMap<String, String>();
        List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();

    }

    /**
     * Reads a file from disk at the specified path.
     * 
     * @param stringPath
     *            path of the file
     * @return a string of a file
     */
    private static String readFromDisk(String stringPath) {
        Path path = Paths.get(stringPath);

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
