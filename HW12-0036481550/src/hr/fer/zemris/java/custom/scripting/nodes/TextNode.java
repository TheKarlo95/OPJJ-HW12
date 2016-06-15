package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * {@code TextNode} is a class extending {@link Node}and is used for holding
 * values from text.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Node
 */
public class TextNode extends Node {

    /**
     * Text which this node holds.
     */
    private String text;

    /**
     * Constructs a {@code TextNode} object.
     * 
     * @param text
     *            text which this node holds
     */
    public TextNode(String text) {
        this.text = Objects.requireNonNull(text, "You cannot instantiate TextNode with null reference as a text!");
    }

    @Override
    public void accept(INodeVisitor visitor) {
        Objects.requireNonNull(visitor, "You cannot visit a TextNode with null reference as a visitor!");
        visitor.visitTextNode(this);
    }

    /**
     * Appends given text to existing one.
     * 
     * @param text
     *            text to be appended
     * @throws NullPointerException
     *             if {@code text} parameter is a {@code null} reference
     */
    public void append(String text) {
        this.text += Objects.requireNonNull(text, "You cannot append null reference as a text!");
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public String asText() {
        return text;
    }

}
