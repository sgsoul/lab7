package common.exceptions;

/**
 * Выбрасывается, когда порт уже используется.
 */

public class PortAlreadyInUseException extends ConnectionException {
    public PortAlreadyInUseException() {
        super("Порт занят.");
    }
}
