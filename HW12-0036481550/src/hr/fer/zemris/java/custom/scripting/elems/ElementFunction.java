package hr.fer.zemris.java.custom.scripting.elems;

/**
 * {@code ElementFunction} class used to make {@code Node} objects that hold a
 * name of a function.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Element
 */
public class ElementFunction extends Element {

    /**
     * Name of this element
     */
    private String name;

    /**
     * Construct {@code ElementFunction} object.
     * 
     * @param name
     *            name of this element
     */
    public ElementFunction(String name) {
        this.name = name;
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