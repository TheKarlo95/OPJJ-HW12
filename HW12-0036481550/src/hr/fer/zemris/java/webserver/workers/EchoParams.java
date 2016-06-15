package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code CircleWorker} is a worker that writes all parameters to context.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IWebWorker
 */
public class EchoParams implements IWebWorker {

    /** Tag for the HTML table. */
    private static String TABLE_TAG = "<table border=\"1\">";

    /** Tag for the header of the HTML table. */
    private static String HEADER_TAG = "<th bgcolor=\"#b3b3b3\" style=\"min-width:300px\" align=\"center\">"
            + "<font color=BLACK face=\"Geneva, Arial\" size=5>";
    /** Tag for the cell of the HTML table. */
    private static String CELL_TAG = "<td style=\"min-width:300px\" align=\"center\">"
            + "<font color=BLACK face=\"Geneva, Arial\" size=4>";

    @Override
    public void processRequest(RequestContext context) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body>");
        sb.append(TABLE_TAG);

        sb.append("<tr>");
        sb.append(HEADER_TAG + "<b>Parameter</b></th>");
        sb.append(HEADER_TAG + "<b>Value</b></th>");
        sb.append("</tr>");

        for (String name : context.getParameterNames()) {
            sb.append("<tr>");
            sb.append(CELL_TAG + name + "</font></td>");
            sb.append(CELL_TAG + context.getParameter(name) + "</font></td>");
            sb.append("</tr>");
        }

        sb.append("</table>");
        sb.append("</body></html>");

        try {
            context.write(sb.toString());
        } catch (IOException ignorable) {
        }
    }

}
