package hr.fer.zemris.java.custom.scripting.elems;

/**
 * {@code ElementConstantDouble} class used to make {@code Node} objects that
 * hold double value.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Element
 */
public class ElementConstantDouble extends Element {

    /**
     * Value of this element.
     */
    private double value;

    /**
     * Construct {@code ElementConstantDouble} object.
     * 
     * @param value
     *            value of this element
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    @Override
    public String asText() {
        return Double.toString(this.value);
    }

    @Override
    public Object getValue() {
        return value;
    }

}
