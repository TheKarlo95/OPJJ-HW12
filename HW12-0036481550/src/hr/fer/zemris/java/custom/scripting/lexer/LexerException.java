package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * <code>LexerException</code> is thrown when <code>Lexer</code> class can't
 * proceed with given input.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class LexerException extends RuntimeException {

    /** Serial version UID. */
    private static final long serialVersionUID = 7900591097543619015L;

    /**
     * Constructs a new {@code LexerException} with {@code null} as its detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public LexerException() {
        super();
    }

    /**
     * Constructs a new {@code LexerException} with the specified detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     * 
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public LexerException(String message) {
        super(message);
    }

}
