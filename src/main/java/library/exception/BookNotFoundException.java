package library.exception;

/**
 * Запрошенная книга не найдена.
 */
public class BookNotFoundException extends Exception {
    
    public BookNotFoundException() {
        super("");
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
