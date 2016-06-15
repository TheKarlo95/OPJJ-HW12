package hr.fer.zemris.java.custom.scripting.elems;

/**
 * {@code ElementVariable} class used to make {@code Node} objects that hold a
 * name of a variable.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class ElementVariable extends Element {

    /**
     * Name of this element
     */
    private String name;

    /**
     * Construct {@code ElementVariable} object.
     * 
     * @param name
     *            name of this element
     */

    public ElementVariable(String name) {
        this.name = name.trim();
    }

    @Override
    public String asText() {
        return name;
    }

    @Override
    public Object getValue() {
        return name;
    }

}
