package hr.fer.zemris.java.custom.scripting.exec.functions;

/**
 * {@code FunctionPParamSet} is a native function of {@code SmartScript} and
 * sets the persistent parameter specified by string and value.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see AbstractFunctionParamDel
 */
public class FunctionPParamSet extends AbstractFunctionParamSet {

    /** Instance of this function. */
    private static FunctionPParamSet FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionPParamSet}.
     */
    private FunctionPParamSet() {
        super((context, name, value) -> context.setPersistentParameter(name, value));

        name = "pparamSet";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionPParamSet getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionPParamSet();
        }

        return FUNCTION;
    }

    @Override
    public String getName() {
        return name;
    }

}
