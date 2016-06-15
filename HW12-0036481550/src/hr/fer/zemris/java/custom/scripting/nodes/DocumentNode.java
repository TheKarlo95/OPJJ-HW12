package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * {@code DocumentNode} is a class extending {@link Node} and holds the values
 * from tokens and uses them for parsing and creation of parsing tree. Used as
 * starting node.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Node
 */
public class DocumentNode extends Node {
    
    @Override
    public void accept(INodeVisitor visitor) {
        Objects.requireNonNull(visitor, "You cannot visit a DocumentNode with null reference as a visitor!");
        visitor.visitDocumentNode(this);
    }

    @Override
    public String toString() {
        return "DocumentNode";
    }

    @Override
    public String asText() {
        return "";
    }
}
