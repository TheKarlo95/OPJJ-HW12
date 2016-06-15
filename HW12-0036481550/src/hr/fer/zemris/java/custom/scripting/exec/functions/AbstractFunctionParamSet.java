package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Objects;
import java.util.Stack;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code AbstractFunctionParamSet} is a abstract class for setting entries from
 * map specified by {@link ThreeConsumer}.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IFunction
 * @see ThreeConsumer
 */
public abstract class AbstractFunctionParamSet implements IFunction {

    /** Specifies the setter method. */
    private ThreeConsumer<RequestContext, String, String> setter;

    /**
     * Constructs a new {@code AbstractFunctionParamDel} with specified
     * {@code setter}.
     * 
     * @param setter
     *            the setter method
     */
    protected AbstractFunctionParamSet(ThreeConsumer<RequestContext, String, String> setter) {
        super();

        this.setter = setter;
    }

    @Override
    public void doOperation(Stack<Object> stack, RequestContext context) {
        Objects.requireNonNull(stack, "You cannot get a parameter with null reference as a stack!");
        Objects.requireNonNull(context, "You cannot get a parameter with null reference as a context!");

        String name = String.valueOf(stack.pop());
        String value = String.valueOf(stack.pop());

        setter.accept(context, name, value);
    }

}
