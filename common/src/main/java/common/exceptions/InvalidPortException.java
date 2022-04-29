package common.exceptions;

/**
 * Выбрасывается, когда порт недействителен.
 */

public class InvalidPortException extends ConnectionException {
    public InvalidPortException() {
        super("Порт недоступен.");
    }
}
