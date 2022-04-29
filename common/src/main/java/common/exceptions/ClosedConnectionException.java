package common.exceptions;

/**
 * Выбрасывается при закрытии соединения
 */

public class ClosedConnectionException extends ConnectionException {
    public ClosedConnectionException() {
        super("Канал сервера закрыт.");
    }
}
