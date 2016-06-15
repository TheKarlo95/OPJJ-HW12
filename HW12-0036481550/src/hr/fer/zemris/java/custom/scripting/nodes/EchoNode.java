package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * {@code EchoNode} is a class extending {@link Node} and is used for holding
 * values for tags.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Node
 */
public class EchoNode extends Node {

    /**
     * Elements of this node.
     */
    private Element[] elements;

    /**
     * Constructs a {@code EchoNode}object.
     * 
     * @param elements
     *            elements to be inserted in this node
     * @throws NullPointerException
     *             if {@code elements} parameter is a {@code null} reference
     */
    public EchoNode(Element[] elements) {
        this.elements = Objects
                .requireNonNull(elements, "You cannot instantiate EchoNode with null reference as a elements!");
    }

    /**
     * Returns the elements contained in this {@code EchoNode}.
     * 
     * @return the elements contained in this {@code EchoNode}
     */
    public Element[] getElements() {
        return elements;
    }

    @Override
    public void accept(INodeVisitor visitor) {
        Objects.requireNonNull(visitor, "You cannot visit a EchoNode with null reference as a visitor!");
        visitor.visitEchoNode(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Element e : elements) {
            if (e == null) {
                break;
            }
            builder.append(e.asText()).append(" ");
        }

        return builder.toString();
    }

    @Override
    public String asText() {
        StringBuilder builder = new StringBuilder();
        builder.append("{$= ").append(toString()).append(" $}");
        return builder.toString();
    }

}
