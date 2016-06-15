package hr.fer.zemris.java.custom.scripting.elems;

/**
 * {@code Element} class used to make {@code Node} objects.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public abstract class Element {

    /**
     * Returns {@code String} representation of {@code Element} object.
     * 
     * @return {@code String} representation of this object.
     */
    public String asText() {
        return "";
    }

    /**
     * Checks if this element represents constant.
     * 
     * @return {@code true} if this element represents constant; {@code false}
     *         otherwise
     */
    public boolean isConstant() {
        return (this instanceof ElementConstantInteger)
                || (this instanceof ElementConstantDouble)
                || (this instanceof ElementString);
    }

    /**
     * Checks if this element represents operator.
     * 
     * @return {@code true} if this element represents operator; {@code false}
     *         otherwise
     */
    public boolean isOperator() {
        return (this instanceof ElementOperator);
    }

    /**
     * Checks if this element represents variable.
     * 
     * @return {@code true} if this element represents variable; {@code false}
     *         otherwise
     */
    public boolean isVariable() {
        return (this instanceof ElementVariable);
    }

    /**
     * Checks if this element represents function.
     * 
     * @return {@code true} if this element represents function; {@code false}
     *         otherwise
     */
    public boolean isFunction() {
        return (this instanceof ElementFunction);
    }

    /**
     * Returns the value of this element.
     * 
     * @return the value of this element
     */
    public abstract Object getValue();

}
