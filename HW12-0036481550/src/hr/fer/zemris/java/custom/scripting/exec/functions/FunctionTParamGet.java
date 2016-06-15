package hr.fer.zemris.java.custom.scripting.exec.functions;

/**
 * {@code FunctionTParamGet} is a native function of {@code SmartScript} and
 * gets the temporary parameter specified by string.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see AbstractFunctionParamDel
 */
public class FunctionTParamGet extends AbstractFunctionParamGet {

    /** Instance of this function. */
    private static FunctionTParamGet FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionTParamGet}.
     */
    private FunctionTParamGet() {
        super((context, name) -> context.getTemporaryParameter(name));

        name = "tparamGet";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionTParamGet getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionTParamGet();
        }

        return FUNCTION;
    }

    @Override
    public String getName() {
        return name;
    }

}
