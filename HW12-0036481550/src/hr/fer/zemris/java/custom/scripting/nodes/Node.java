package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@code Node} class is used for holding values from tokens, for parsing and
 * creation of parse tree.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public abstract class Node {

    /**
     * Children of this node.
     */
    private List<Node> children;

    /**
     * Method that enables visits from a {@link INodeVisitor} object.
     * 
     * @param visitor
     *            the visitor which is going to visit instance of this class
     */
    public abstract void accept(INodeVisitor visitor);

    /**
     * Returns the textual representation of a node.
     * <p>
     * <b>Note:</b>This method is similar to {@linkplain #toString()} but
     * doesn't have to be the same. In method {@linkplain #toString()} some
     * parts are omitted.
     * 
     * @return the textual representation of a node
     */
    public abstract String asText();

    /**
     * Adds a child to this node.
     * 
     * @param child
     *            node to be added as a child
     * @throws NullPointerException
     *             if {@code child} parameter is a {@code null} reference
     */
    public void addChildNode(Node child) {
        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(Objects.requireNonNull(child, "You cannot add a null reference as child node!"));
    }

    /**
     * Returns the number of children.
     * 
     * @return the number of children
     */
    public int numberOfChildren() {
        return children == null ? 0 : children.size();
    }

    /**
     * Returns the child at given index.
     * 
     * @param index
     *            index of a child
     * @return child node
     * @throws IndexOutOfBoundsException
     *             if the {@code index} is out of range
     */
    public Node getChild(int index) {
        if (children == null)
            return null;

        if (index < 0 || index >= children.size()) {
            throw new IndexOutOfBoundsException("Index should be between 0 and " + (children.size() - 1) + "!");
        }

        return children.get(index);
    }

    /**
     * Returns all of the children.
     * 
     * @return all children
     */
    public Node[] getAllChildren() {
        int size = numberOfChildren();
        Node[] children = new Node[size];

        for (int i = 0; i < size; i++) {
            children[i] = getChild(i);
        }

        return children;
    }

}
