package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code FunctionDup} is a native function of {@code SmartScript} and
 * duplicates the number.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IFunction
 */
public class FunctionDup implements IFunction {

    /** Instance of this function. */
    private static FunctionDup FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionDup}.
     */
    private FunctionDup() {
        name = "dup";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionDup getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionDup();
        }

        return FUNCTION;
    }

    @Override
    public void doOperation(Stack<Object> stack, RequestContext context) {
        Objects.requireNonNull(stack, "You cannot do a duplication oepration with null reference as a stack!");

        Object value = stack.peek();

        stack.push(value);
    }

    @Override
    public String getName() {
        return name;
    }

}
