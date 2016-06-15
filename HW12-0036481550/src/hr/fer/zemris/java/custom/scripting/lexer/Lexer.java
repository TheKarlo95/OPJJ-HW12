package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * {@code Lexer} is lexical analyzer which takes a string and splits it into a
 * series of {@code Token} objects.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class Lexer {

    /**
     * String constant used for indicating start of a tag
     */
    private static final String START = "{$";

    /**
     * String constant used for indicating end of a tag.
     */
    private static final String END = "$}";

    /**
     * {@code char} array for input data
     */
    private char[] data;

    /**
     * Last used {@code Token}
     */
    private Token token;

    /**
     * Current index is used to remember where we stopped reading data last time
     * we used {@linkplain #nextToken()} method.
     */
    private int currentIndex;

    /**
     * Current state of {@code Lexer} class
     */
    private LexerState state;

    /**
     * Constructs a {@code Lexer} class from {@code String} parameter.
     * 
     * @param text
     *            text to be split into a series of {@code Token} objects.
     */
    public Lexer(String text) {
        if (text == null) {
            throw new IllegalArgumentException("You tried to run lexical " + "analyzer on null reference");
        }

        this.data = text.trim().toCharArray();
        this.token = null;
        this.currentIndex = 0;

        if (text.trim().startsWith(START)) {
            this.state = LexerState.TAG;
            this.currentIndex = 2;
        } else {
            this.state = LexerState.TEXT;
        }
    }

    /**
     * Returns last found {@code Token}.
     * 
     * @return last found {@code Token}
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Returns next {@code Token} data in data predefined with constructor.
     * <code>LexerException</code> can be thrown if bad input is given.
     * 
     * @return next {@code Token}
     */
    public Token nextToken() {
        String value = "";
        boolean flag = false;

        if (currentIndex >= data.length) {
            return new Token(TokenType.EOF, null);
        }

        do {
            value += data[currentIndex];
            currentIndex++;

            if (value.trim().startsWith(START)) {
                state = LexerState.TAG;
            } else if (value.trim().endsWith(END)) {
                flag = true;
            }
        } while (canProceed(value));

        value = adjustValue(value);
        this.token = new Token(getTokenType(value), value);

        if (flag) {
            state = LexerState.TEXT;
        }

        return this.token;
    }

    /**
     * Returns current state of {@code Lexer} class.
     * 
     * @return current state of {@code Lexer} class
     */
    public LexerState getState() {
        return this.state;
    }

    /**
     * Removes unnecessary backslashes
     * 
     * @param value
     *            {@code String} value of a token
     * @return adjusted {@code String} value of a token
     */
    private String adjustValue(String value) {
        if (state.equals(LexerState.TAG)) {
            value = value.replaceAll("\\\\n", "\n");
            value = value.replaceAll("\\\\r", "\r");
            value = value.replaceAll("\\\\\\\\", "\\");
            value = value.trim();
        }
        return value;
    }

    /**
     * Tells {@code Lexer} if it can proceed with its work
     * 
     * @param value
     *            {@code String} value of a token
     * @return {@code true} if it can proceed and {@code false} otherwise
     */
    private boolean canProceed(String value) {
        if (currentIndex >= data.length) {
            return false;
        } else if (data[currentIndex] == '{') {
            return false;
        } else if (value.trim().isEmpty()) {
            return true;
        } else if (!isValidToken(value)) {
            return true;
        } else if (Character.isWhitespace(data[currentIndex]) && !getTokenType(value).equals(TokenType.TEXT)) {
            return false;
        } else if (isValidToken(value) && !isValidToken(value + data[currentIndex])) {
            return false;
        } else if (isValidToken(value) && isValidToken(value + data[currentIndex])) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if value of a {@code Token} is valid.
     * 
     * @param value
     *            value of a {@code Token}
     * @return {@code true} if value is valid and {@code false} otherwise
     */
    private boolean isValidToken(String value) {
        if (value.startsWith("\\\"")) {
            return true;
        }

        try {
            getTokenType(value);
        } catch (LexerException e) {
            return false;
        }

        return true;
    }

    /**
     * Checks if value of a {@code Token} is valid.
     * 
     * @param str
     *            value of a {@code Token}
     * @return {@code TokenType} of a {@code Token}
     */
    public TokenType getTokenType(String str) {
        if (state.equals(LexerState.TEXT)) {
            return TokenType.TEXT;
        }

        for (TokenType type : TokenType.values()) {
            if (type.getPattern().matcher(str.trim()).matches()
                    && !type.equals(TokenType.TEXT)) {
                return type;
            }
        }

        throw new LexerException("Invalid token value!");
    }

}
