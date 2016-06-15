package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code FunctionSetMimeType} is a native function of {@code SmartScript} and
 * sets the mime type of HTTP request.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IFunction
 */
public class FunctionSetMimeType implements IFunction {

    /** Instance of this function. */
    private static FunctionSetMimeType FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionSetMimeType}.
     */
    private FunctionSetMimeType() {
        super();

        name = "setMimeType";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionSetMimeType getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionSetMimeType();
        }

        return FUNCTION;
    }

    @Override
    public void doOperation(Stack<Object> stack, RequestContext context) {
        Objects.requireNonNull(stack, "You cannot set a mime type with null reference as a stack!");
        Objects.requireNonNull(context, "You cannot set a mime type with null reference as a context!");

        String mimeType = (String) stack.pop();

        context.setMimeType(mimeType);
    }

    @Override
    public String getName() {
        return name;
    }

}
