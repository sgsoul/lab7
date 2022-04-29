package common.exceptions;

/**
 * Выбрасывается, когда формат даты недопустим.
 */

public class InvalidDateFormatException extends InvalidDataException {
    public InvalidDateFormatException() {
        super("Формат даты: гггг-мм-дд");
    }
}
