package hr.fer.zemris.java.custom.scripting.elems;

/**
 * {@code ElementString} class used to make {@code Node} objects that hold a
 * string.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class ElementString extends Element {

    /**
     * Value of this element
     */
    private String value;

    /**
     * Construct {@code ElementString} object.
     * 
     * @param value
     *            value of this element
     */
    public ElementString(String value) {
        this.value = value;
    }

    @Override
    public String asText() {
        return "\"" + this.value + "\"";
    }

    @Override
    public Object getValue() {
        return value;
    }

}