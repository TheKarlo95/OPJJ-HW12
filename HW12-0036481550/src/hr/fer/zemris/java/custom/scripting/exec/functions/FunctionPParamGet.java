package hr.fer.zemris.java.custom.scripting.exec.functions;

/**
 * {@code FunctionPParamGet} is a native function of {@code SmartScript} and
 * gets the persistent parameter specified by string.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see AbstractFunctionParamDel
 */
public class FunctionPParamGet extends AbstractFunctionParamGet {

    /** Instance of this function. */
    private static FunctionPParamGet FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionPParamGet}.
     */
    private FunctionPParamGet() {
        super((context, name) -> context.getPersistentParameter(name));

        name = "pparamGet";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionPParamGet getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionPParamGet();
        }

        return FUNCTION;
    }

    @Override
    public String getName() {
        return name;
    }

}
