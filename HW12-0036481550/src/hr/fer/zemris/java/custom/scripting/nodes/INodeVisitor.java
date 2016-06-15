package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * {@code INodeVisitor} is class that goes through all implementations of the
 * {@link Node} class and add appropriate accept method in order to build into
 * them a support for Visitor design pattern.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Node
 */
public interface INodeVisitor {

    /**
     * Visits the {@link TextNode}.
     * 
     * @param node
     *            {@link TextNode} that is going to be visited
     */
    public void visitTextNode(TextNode node);

    /**
     * Visits the {@link ForLoopNode}.
     * 
     * @param node
     *            {@link ForLoopNode} that is going to be visited
     */
    public void visitForLoopNode(ForLoopNode node);

    /**
     * Visits the {@link EchoNode}.
     * 
     * @param node
     *            {@link EchoNode} that is going to be visited
     */
    public void visitEchoNode(EchoNode node);

    /**
     * Visits the {@link DocumentNode}.
     * 
     * @param node
     *            {@link DocumentNode} that is going to be visited
     */
    public void visitDocumentNode(DocumentNode node);
}
