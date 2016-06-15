package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Objects;
import java.util.Stack;
import java.util.function.BiConsumer;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code AbstractFunctionParamDel} is a abstract class for deleting entries
 * from map specified by {@link BiConsumer}.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IFunction
 */
public abstract class AbstractFunctionParamDel implements IFunction {

    /** Specifies the delete method. */
    private BiConsumer<RequestContext, String> deleter;

    /**
     * Constructs a new {@code AbstractFunctionParamDel} with specified
     * {@code deleter}.
     * 
     * @param deleter
     *            the delete method
     */
    protected AbstractFunctionParamDel(BiConsumer<RequestContext, String> deleter) {
        super();

        this.deleter = deleter;
    }

    @Override
    public void doOperation(Stack<Object> stack, RequestContext context) {
        Objects.requireNonNull(stack, "You cannot get a parameter with null reference as a stack!");
        Objects.requireNonNull(context, "You cannot get a parameter with null reference as a context!");

        String name = String.valueOf(stack.pop());

        deleter.accept(context, name);
    }

}
