package hr.fer.zemris.java.custom.scripting.exec.functions;

/**
 * {@code FunctionParamGet} is a native function of {@code SmartScript} and gets
 * the parameter specified by string.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see AbstractFunctionParamGet
 */
public class FunctionParamGet extends AbstractFunctionParamGet {

    /** Instance of this function. */
    private static FunctionParamGet FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionParamGet}.
     */
    private FunctionParamGet() {
        super((context, name) -> context.getParameter(name));

        name = "paramGet";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionParamGet getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionParamGet();
        }

        return FUNCTION;
    }

    @Override
    public String getName() {
        return name;
    }

}
