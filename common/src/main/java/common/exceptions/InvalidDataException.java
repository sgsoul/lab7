package common.exceptions;

/**
 * Неверные данные
 */

public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}