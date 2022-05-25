package common.exceptions;

/**
 * Выбрасывается, когда время ожидания превышает
 */

public class ConnectionTimeoutException extends ConnectionException {
    public ConnectionTimeoutException() {
        super("[TimeoutException] Время ожидания ответа истекло.");
    }
}
