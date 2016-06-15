package hr.fer.zemris.java.custom.scripting.exec.functions;

/**
 * {@code FunctionTParamDel} is a native function of {@code SmartScript} and
 * deletes the temporary parameter specified by string.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see AbstractFunctionParamDel
 */
public class FunctionTParamDel extends AbstractFunctionParamDel {

    /** Instance of this function. */
    private static FunctionTParamDel FUNCTION;

    /** Name of this function */
    private String name;

    /**
     * Constructs a new {@code FunctionTParamDel}.
     */
    private FunctionTParamDel() {
        super((context, name) -> context.removeTemporaryParameter(name));
        
        name = "tparamDel";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionTParamDel getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionTParamDel();
        }

        return FUNCTION;
    }

    @Override
    public String getName() {
        return name;
    }
    
}
