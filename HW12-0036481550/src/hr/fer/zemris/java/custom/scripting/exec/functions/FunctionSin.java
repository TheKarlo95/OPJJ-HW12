package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code FunctionDup} is a native function of {@code SmartScript} and
 * calculates the sine of a number.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IFunction
 */
public class FunctionSin implements IFunction {

    /** Instance of this function. */
    private static FunctionSin FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionSin}.
     */
    private FunctionSin() {
        super();

        name = "sin";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionSin getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionSin();
        }

        return FUNCTION;
    }

    @Override
    public void doOperation(Stack<Object> stack, RequestContext context) {
        Objects.requireNonNull(stack, "You cannot do a sine oepration with null reference as a stack!");

        String number = String.valueOf(stack.pop());

        double result = Math.sin(Math.toRadians(Double.valueOf(number)));

        stack.push(result);
    }

    @Override
    public String getName() {
        return name;
    }

}
