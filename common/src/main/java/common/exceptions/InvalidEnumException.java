package common.exceptions;

/**
 * Выдается, когда входные данные не соответствуют перечислению.
 */

public class InvalidEnumException extends InvalidDataException {
    public InvalidEnumException() {
        super("Такой константы не существует.");
    }

    public InvalidEnumException(String s) {
        super(s);
    }
}
