package library.exception;

/**
 * Попытка повторной вставки существующего объекта.
 */
public class DuplicateException extends Exception {

    public DuplicateException() {
        super("");
    }

    public DuplicateException(String message) {
        super(message);
    }
}
