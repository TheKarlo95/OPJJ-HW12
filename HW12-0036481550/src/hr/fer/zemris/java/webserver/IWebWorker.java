package hr.fer.zemris.java.webserver;

/**
 * {@code IWebWorker} is an interface for workers that will create output for a
 * HTTP request.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public interface IWebWorker {

    /**
     * Processes the HTTP request.
     * 
     * @param context
     *            request context
     */
    public void processRequest(RequestContext context);

}
