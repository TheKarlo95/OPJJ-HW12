package hr.fer.zemris.java.custom.scripting.elems;

/**
 * {@code ElementOperator} class used to make {@code Node} objects that hold a
 * symbol of a operator.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Element
 */
public class ElementOperator extends Element {

    /**
     * Symbol of this element
     */
    private String symbol;

    /**
     * Construct {@code ElementOperator} object.
     * 
     * @param symbol
     *            symbol of this element
     */
    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String asText() {
        return this.symbol;
    }

    @Override
    public Object getValue() {
        return symbol;
    }

}