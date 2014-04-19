package library.exception;

/**
 * Запрошенный автор не найден.
 */
public class AuthorNotFoundException extends Exception {
    
    public AuthorNotFoundException() {
        super("");
    }

    public AuthorNotFoundException(String message) {
        super(message);
    }
}
