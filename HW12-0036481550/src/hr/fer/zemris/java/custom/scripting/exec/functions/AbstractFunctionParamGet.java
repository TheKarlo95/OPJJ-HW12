package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Objects;
import java.util.Stack;
import java.util.function.BiFunction;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code AbstractFunctionParamGet} is a abstract class for getting entries
 * from map specified by {@link BiFunction}.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IFunction
 */
public abstract class AbstractFunctionParamGet implements IFunction {

    /** Specifies the getter method. */
    private BiFunction<RequestContext, String, String> getter;

    /**
     * Constructs a new {@code AbstractFunctionParamDel} with specified
     * {@code getter}.
     * 
     * @param getter
     *            the getter method
     */
    protected AbstractFunctionParamGet(BiFunction<RequestContext, String, String> getter) {
        super();
        
        this.getter = getter;
    }

    @Override
    public void doOperation(Stack<Object> stack, RequestContext context) {
        Objects.requireNonNull(stack, "You cannot get a parameter with null reference as a stack!");
        Objects.requireNonNull(context, "You cannot get a parameter with null reference as a context!");

        Object defaultValue = stack.pop();
        String name = String.valueOf(stack.pop());
        
        String value = getter.apply(context, name);

        stack.push(value != null ? value : defaultValue);
    }

}
