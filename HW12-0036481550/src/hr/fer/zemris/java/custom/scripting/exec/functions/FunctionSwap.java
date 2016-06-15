package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code FunctionDup} is a native function of {@code SmartScript} and
 * swaps the last two numbers on the stack.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IFunction
 */
public class FunctionSwap implements IFunction {

    /** Instance of this function. */
    private static FunctionSwap FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionSwap}.
     */
    private FunctionSwap() {
        super();

        name = "swap";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionSwap getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionSwap();
        }

        return FUNCTION;
    }

    @Override
    public void doOperation(Stack<Object> stack, RequestContext context) {
        Objects.requireNonNull(stack, "You cannot do a swap oepration with null reference as a stack!");

        Object value1 = stack.pop();
        Object value2 = stack.pop();

        stack.push(value1);
        stack.push(value2);
    }

    @Override
    public String getName() {
        return name;
    }

}
