package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;

/**
 * <code>Token</code> is class which is holding value and it's type. It is used
 * in <code>Lexer</code> to split <code>String</code> in <code>Token</code>
 * objects.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 *
 */
public class Token {

	/**
	 * Type of a token.
	 */
	private TokenType type;
	
	/**
	 * Value that token holds.
	 */
	private Object value;

	/**
	 * Constructs a <code>Token</code> object.
	 * 
	 * @param type type of a token
	 * @param value value that token holds
	 */
	public Token(TokenType type, String value) {
		this.type = type;

		switch (type) {
		case VARIABLE:
		case KEYWORD:
		case TAGNAME:
		case OPERATOR:
		case START:
		case END:
			this.value = value.trim();
			break;
		case TEXT:
		    this.value = value;
            break;
		case FUNCTION:
			this.value = value.substring(1).trim();
			break;
		case STRING:
			this.value = value.substring(1, value.length() - 1);
			break;
		case INTEGER:
			this.value = Integer.parseInt(value.trim());
			break;
		case DOUBLE:
			this.value = Double.parseDouble(value.trim());
			break;
		case EOF:
			this.value = null;
			break;
		default:
			throw new LexerException("Unknown token type!");
		}
	}

	/**
	 * Returns the value of token.
	 * 
	 * @return value of token
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * Returns the type of token.
	 * 
	 * @return type of token
	 */
	public TokenType getType() {
		return this.type;
	}

	/**
	 * Converts <code>Token</code> to <code>Element</code>
	 * 
	 * @return <code>Element</code> representation of this <code>Token</code>
	 */
	public Element toElement() {
		if (type.equals(TokenType.INTEGER)) {
			return new ElementConstantInteger((int) value);
		} else if (type.equals(TokenType.DOUBLE)) {
			return new ElementConstantDouble((double) value);
		} else if (type.equals(TokenType.FUNCTION)) {
			return new ElementFunction((String) value);
		} else if (type.equals(TokenType.OPERATOR)) {
			return new ElementOperator((String) value);
		} else if (type.equals(TokenType.STRING)) {
			return new ElementString((String) value);
		} else if (type.equals(TokenType.VARIABLE)) {
			return new ElementVariable((String) value);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" + this.type + ", " + this.value + ")";
	}
}
