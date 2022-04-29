package common.exceptions;

/**
 * Выбрасывается, когда введенное число неверно.
 */

public class InvalidNumberException extends InvalidDataException {
    public InvalidNumberException() {
        super("Недопустимый тип числа.");
    }

    public InvalidNumberException(String msg) {
        super(msg);
    }
}
