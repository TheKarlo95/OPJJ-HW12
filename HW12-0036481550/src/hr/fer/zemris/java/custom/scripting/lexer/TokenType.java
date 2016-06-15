package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.regex.Pattern;

/**
 * Possible states of a <code>Token</code>:
 * <p>- EOF (end of line)</p>
 * <p>- KEYWORD</p>
 * <p>- VARIABLE</p>
 * <p>- FUNCTION</p>
 * <p>- TAGNAME</p>
 * <p>- OPERATOR</p>
 * <p>- INTEGER</p>
 * <p>- DOUBLE</p>
 * <p>- STRING</p>
 * <p>- START</p>
 * <p>- END</p>
 * <p>- TEXT</p>
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public enum TokenType {
    /** End of file token type. */
	EOF("")
	/** Keyword token type. */
	, KEYWORD("^(\\s*for\\s*|\\s*end\\s*|\\s*FOR\\s*|\\s*END\\s*)$")
	/** Variable token type. */
	, VARIABLE("^\\s*\\p{L}[\\p{L}\\d_]*\\s*$")
	/** Function token type. */
	, FUNCTION("^\\s*@\\p{L}[\\p{L}\\d_]*\\s*$")
	/** Tag name token type. */
	, TAGNAME("^\\s*(=|\\p{L}[\\p{L}\\d_]*)\\s*$")
	/** Operator token type. */
	, OPERATOR("^\\s*[\\+\\-\\*/^]\\s*$")
	/** Integer constant token type. */
	, INTEGER("^\\s*[+-]?\\d+\\s*$")
	/** Double constant token type. */
	, DOUBLE("^\\s*[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\s*$")
	/** String constant token type. */
	, STRING("^\\\"(.|\\n|\\r)*\\\"$")
	/** Start of tag token type. */
	, START("^\\{\\$$")
	/** End of tag token type. */
	, END("^\\$\\}$")
	/** Text token type. */
	, TEXT("^\\s*[.\b]*\\s*$");
	
	/** The pattern representing this token type. */
	private Pattern pattern;

	/**
	 * Constructs a new {@code TokenType} with specified {@code regex} parameter.
	 * 
	 * @param regex the regular expression to be compiled
	 */
	private TokenType(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	/**
	 * Returns the pattern representing this token type.
	 * 
	 * @return the pattern
	 */
	public Pattern getPattern() {
		return this.pattern;
	}
	
}
