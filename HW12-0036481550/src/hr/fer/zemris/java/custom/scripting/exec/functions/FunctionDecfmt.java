package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code FunctionDecfmt} is a native function of {@code SmartScript} and
 * formats the number.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IFunction
 */
public class FunctionDecfmt implements IFunction {

    /** Instance of this function. */
    private static FunctionDecfmt FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionDecfmt}.
     */
    private FunctionDecfmt() {
        name = "decfmt";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionDecfmt getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionDecfmt();
        }

        return FUNCTION;
    }

    @Override
    public void doOperation(Stack<Object> stack, RequestContext context) {
        Objects.requireNonNull(stack, "You cannot do a decimal format oepration with null reference as a stack!");

        String pattern = (String) stack.pop();
        String number = String.valueOf(stack.pop());

        stack.push(new DecimalFormat(pattern).format(Double.valueOf(number)));
    }

    @Override
    public String getName() {
        return name;
    }

}
