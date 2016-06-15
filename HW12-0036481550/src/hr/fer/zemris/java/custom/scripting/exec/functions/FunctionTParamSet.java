package hr.fer.zemris.java.custom.scripting.exec.functions;

/**
 * {@code FunctionTParamSet} is a native function of {@code SmartScript} and
 * sets the temporary parameter specified by string and value.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see AbstractFunctionParamDel
 */
public class FunctionTParamSet extends AbstractFunctionParamSet {

    /** Instance of this function. */
    private static FunctionTParamSet FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionTParamSet}.
     */
    private FunctionTParamSet() {
        super((context, name, value) -> context.setTemporaryParameter(name, value));

        name = "tparamSet";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionTParamSet getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionTParamSet();
        }

        return FUNCTION;
    }

    @Override
    public String getName() {
        return name;
    }

}
