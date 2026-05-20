package src.exception;

public class LigaException extends Exception {
    public LigaException(String message) {
        super(message);
    }

    public LigaException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
