package common.exceptions;

/**
 * Выбрасывается, когда полученные данные недействительны.
 */

public class InvalidReceivedDataException extends InvalidDataException {
    public InvalidReceivedDataException() {
        super("Полученные данные повреждены.");
    }
}
