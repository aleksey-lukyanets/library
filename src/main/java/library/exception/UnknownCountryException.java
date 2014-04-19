package library.exception;

/**
 * Попытка сослаться на несуществующую страну.
 */
public class UnknownCountryException extends Exception {
    
    public UnknownCountryException() {
        super("");
    }

    public UnknownCountryException(String message) {
        super(message);
    }
}
