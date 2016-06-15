package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * {@code TreeWriter} is demonstrative class that demonstrates the use and
 * features of the implementations of a {@link INodeVisitor} interface.
 * <p>
 * User should give path to a SmartScript file trough as a command-line
 * argument.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see INodeVisitor
 */
public class TreeWriter {

    /**
     * Starting point of a program.
     * 
     * @param args
     *            Command-line argument
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("You should input a path to a file with the SmartScript!");
            return;
        }

        Path path = Paths.get(args[0]);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            System.err.println("Datoteka ne postoji.");
            return;
        }

        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Some I/O error occured while reading from the file" + path.toAbsolutePath() + "!");
            return;
        }

        String docBody = new String(data, StandardCharsets.UTF_8);

        SmartScriptParser p = new SmartScriptParser(docBody);
        
        INodeVisitor visitor = new WriteVisitor();
        p.getDocumentNode().accept(visitor);
    }

    /**
     * {@code WriteVisitor} is a visitor class that visits the {@link Node}
     * objects and then prints out whatever their method {@link Node#asText()}
     * returns.
     * 
     * @author Karlo Vrbić
     * @version 1.0
     * @see INodeVisitor
     */
    private static class WriteVisitor implements INodeVisitor {

        @Override
        public void visitTextNode(TextNode node) {
            System.out.print(node.asText() + "\n");
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            System.out.print(node.asText() + "\n");
            
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            System.out.print(node.asText() + "\n");
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }

    }
}
