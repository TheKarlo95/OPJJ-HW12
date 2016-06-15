package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionDecfmt;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionDup;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionPParamDel;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionPParamGet;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionPParamSet;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionParamGet;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionSetMimeType;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionSin;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionSwap;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionTParamDel;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionTParamGet;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionTParamSet;
import hr.fer.zemris.java.custom.scripting.exec.functions.IFunction;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code SmartScriptEngine} is class that runs the {@code SmartScript} scripts.
 * 
 * @author Karlo Vrbić
 *
 */
public class SmartScriptEngine {

    /** Map of all functions. */
    private static Map<String, IFunction> FUNCTIONS;

    /** The document node. */
    private DocumentNode documentNode;
    /** Current request context. */
    private RequestContext requestContext;
    /** Multistack for scripts. */
    private ObjectMultistack multistack = new ObjectMultistack();
    /** Visitor for nodes. */
    private INodeVisitor visitor = new InterpreterVisitor();

    static {
        FUNCTIONS = new HashMap<>(12);

        FUNCTIONS.put(FunctionDecfmt.getInstance().getName(), FunctionDecfmt.getInstance());
        FUNCTIONS.put(FunctionDup.getInstance().getName(), FunctionDup.getInstance());
        FUNCTIONS.put(FunctionParamGet.getInstance().getName(), FunctionParamGet.getInstance());
        FUNCTIONS.put(FunctionPParamDel.getInstance().getName(), FunctionPParamDel.getInstance());
        FUNCTIONS.put(FunctionPParamGet.getInstance().getName(), FunctionPParamGet.getInstance());
        FUNCTIONS.put(FunctionPParamSet.getInstance().getName(), FunctionPParamSet.getInstance());
        FUNCTIONS.put(FunctionTParamDel.getInstance().getName(), FunctionTParamDel.getInstance());
        FUNCTIONS.put(FunctionTParamGet.getInstance().getName(), FunctionTParamGet.getInstance());
        FUNCTIONS.put(FunctionTParamSet.getInstance().getName(), FunctionTParamSet.getInstance());
        FUNCTIONS.put(FunctionSetMimeType.getInstance().getName(), FunctionSetMimeType.getInstance());
        FUNCTIONS.put(FunctionSin.getInstance().getName(), FunctionSin.getInstance());
        FUNCTIONS.put(FunctionSwap.getInstance().getName(), FunctionSwap.getInstance());
    }

    /**
     * Constructs a new {@code SmartScriptEngine} with specified document node
     * and HTTP request context.
     * 
     * @param documentNode
     *            the document node
     * @param requestContext
     *            HTTP request context
     */
    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = Objects.requireNonNull(
                documentNode,
                "You cannot instantiate SmartScriptEngine with null as a document node!");
        this.requestContext = Objects.requireNonNull(
                requestContext,
                "You cannot instantiate SmartScriptEngine with null as a request code!");
    }

    /**
     * Executes the script.
     */
    public void execute() {
        documentNode.accept(visitor);
    }

    /**
     * {@code InterpreterVisitor} is a visitor class that interprets the script.
     * 
     * @author Karlo Vrbić
     * @version 1.0
     * @see INodeVisitor
     */
    private class InterpreterVisitor implements INodeVisitor {

        @Override
        public void visitTextNode(TextNode node) {
            try {
                requestContext.write(node.asText());
            } catch (IOException e) {
                System.err.println("Some I/O error occured while writing to output stream!");
                return;
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            String variable = (String) node.getVariable().getValue();

            Object endExpression = node.getEndExpression().getValue();
            Object stepExpression = node.getStepExpression().getValue();

            multistack.push(variable, new ValueWrapper(node.getStartExpression().getValue()));

            while (multistack.peek(variable).numCompare(endExpression) <= 0) {
                for (Node child : node.getAllChildren()) {
                    child.accept(visitor);
                }

                multistack.push(variable, multistack.pop(variable).increment(stepExpression));
            }
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            Stack<Object> stack = new Stack<>();
            Element[] elements = node.getElements();

            for (int i = 0; i < elements.length; i++) {
                Element element = elements[i];

                if (element == null)
                    continue;

                if (element.isConstant()) {
                    stack.push(element.getValue());
                } else if (element.isVariable()) {
                    stack.push(multistack.peek((String) element.getValue()).getValue());
                } else if (element.isOperator()) {
                    Object num1 = stack.pop();
                    Object num2 = stack.pop();

                    switch ((String) element.getValue()) {
                        case "+":
                            stack.push(new ValueWrapper(num1).increment(num2).getValue());
                            break;
                        case "-":
                            stack.push(new ValueWrapper(num1).decrement(num2).getValue());
                            break;
                        case "*":
                            stack.push(new ValueWrapper(num1).multiply(num2).getValue());
                            break;
                        case "/":
                            stack.push(new ValueWrapper(num1).divide(num2).getValue());
                            break;
                    }
                } else if (element.isFunction()) {
                    FUNCTIONS.get(element.getValue()).doOperation(stack, requestContext);
                }
            }

            if (!stack.isEmpty()) {
                List<Object> inversedStack = new ArrayList<>(stack);

                for (Object obj : inversedStack) {
                    try {
                        requestContext.write(String.valueOf(obj));
                    } catch (IOException e) {
                    }
                }
            }
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }

    }
}
