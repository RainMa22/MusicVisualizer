package me.rainma22.intermediateimage.Resources;

public class WrongResourceTypeException extends RuntimeException {
    public WrongResourceTypeException() {
        super();
    }

    public WrongResourceTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongResourceTypeException(Throwable cause) {
        super(cause);
    }

    public WrongResourceTypeException(String message) {
        super(message);
    }
}
