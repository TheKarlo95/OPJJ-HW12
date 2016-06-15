package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * {@code ForLoopNode} is a class extending {@link Node} and is used for holding
 * values for for loops.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Node
 */
public class ForLoopNode extends Node {

    /**
     * Variable of a for loop.
     */
    private ElementVariable variable;

    /**
     * Start expression of a for loop.
     */
    private Element startExpression;

    /**
     * End expression of a for loop.
     */
    private Element endExpression;

    /**
     * Step expression of a for loop.(can be null)
     */
    private Element stepExpression;

    /**
     * Constructs a {@code ForLoopNode} object.
     * 
     * @param variable
     *            variable for a for loop
     * @param startExpression
     *            start expression of a for loop
     * @param endExpression
     *            end expression of a for loop
     * @param stepExpression
     *            step expression of a for loop.(can be null)
     * @throws NullPointerException
     *             if {@code text}, {@code startExpression} or
     *             {@code endExpression} parameters are a {@code null} reference
     */
    public ForLoopNode(Element variable, Element startExpression, Element endExpression,
            Element stepExpression) {
        this.variable = (ElementVariable) Objects
                .requireNonNull(variable, "You cannot instantiate ForLoopNode with null reference as a variable!");
        this.startExpression = Objects.requireNonNull(
                startExpression,
                "You cannot instantiate ForLoopNode with null reference as a start expression!");
        this.endExpression = Objects.requireNonNull(
                endExpression,
                "You cannot instantiate ForLoopNode with null reference as a end expression!");
        this.stepExpression = stepExpression;
    }

    @Override
    public void accept(INodeVisitor visitor) {
        Objects.requireNonNull(visitor, "You cannot visit a ForLoopNode with null reference as a visitor!");
        visitor.visitForLoopNode(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(variable.asText() + " ");
        builder.append(startExpression.asText() + " ");
        builder.append(endExpression.asText() + " ");

        if (stepExpression != null) {
            builder.append(stepExpression.asText());
        }

        return builder.toString();
    }

    @Override
    public String asText() {
        StringBuilder builder = new StringBuilder();

        builder.append("{$ FOR ").append(toString()).append(" $}\n");

        for (Node child : getAllChildren()) {
            builder.append(child.asText()).append("\n");
        }

        builder.append("{$ END $}");

        return builder.toString();
    }

    /**
     * Returns the variable element.
     * 
     * @return the variable element
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * Returns the start expression element.
     * 
     * @return the start expression element
     */
    public Element getStartExpression() {
        return startExpression;
    }

    /**
     * Returns the end expression element.
     * 
     * @return the end expression element
     */
    public Element getEndExpression() {
        return endExpression;
    }

    /**
     * Returns the step expression element.
     * 
     * @return the step expression element
     */
    public Element getStepExpression() {
        return stepExpression;
    }

}
