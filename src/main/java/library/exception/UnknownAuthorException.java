package library.exception;

/**
 * Попытка сослаться на несуществующего автора.
 */
public class UnknownAuthorException extends Exception {
    
    public UnknownAuthorException() {
        super("");
    }

    public UnknownAuthorException(String message) {
        super(message);
    }
}
