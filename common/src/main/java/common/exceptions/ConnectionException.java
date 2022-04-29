package common.exceptions;

/**
 * Ошибки подключения
 */

public class ConnectionException extends Exception {
    public ConnectionException(String s) {
        super(s);
    }
}
