package hr.fer.zemris.java.custom.scripting.exec.functions;

/**
 * {@code FunctionPParamDel} is a native function of {@code SmartScript} and
 * deletes the persistent parameter specified by string.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see AbstractFunctionParamDel
 */
public class FunctionPParamDel extends AbstractFunctionParamDel {

    /** Instance of this function. */
    private static FunctionPParamDel FUNCTION;

    /** Name of this function */
    private String name;
    
    /**
     * Constructs a new {@code FunctionPParamDel}.
     */
    private FunctionPParamDel() {
        super((context, name) -> context.removePersistentParameter(name));
        
        name = "pparamDel";
    }

    /**
     * Returns the instance of this function.
     * 
     * @return the instance of this function
     */
    public static FunctionPParamDel getInstance() {
        if (FUNCTION == null) {
            FUNCTION = new FunctionPParamDel();
        }

        return FUNCTION;
    }
    
    @Override
    public String getName() {
        return name;
    }

}
