package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code IFunction} is an interface for all native {@code SmartScript}
 * functions.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public interface IFunction {

    /**
     * Executes the operation of this function.
     * 
     * @param stack
     *            stack of the current script
     * @param context
     *            context of the script
     */
    public void doOperation(Stack<Object> stack, RequestContext context);

    /**
     * Returns the name of this function.
     * 
     * @return the name of this function
     */
    public String getName();

}
