package hr.fer.zemris.java.custom.scripting.elems;

/**
 * {@code ElementConstantInteger} class used to make {@code Node} objects that
 * hold integer value.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Element
 */
public class ElementConstantInteger extends Element {

    /**
     * Value of this element.
     */
    private int value;

    /**
     * Construct {@code ElementConstantInteger} object.
     * 
     * @param value
     *            value of this element
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    @Override
    public String asText() {
        return Integer.toString(this.value);
    }

    @Override
    public Object getValue() {
        return value;
    }

}
