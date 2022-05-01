package common.exceptions;

public class AuthException extends ConnectionException {
    public AuthException(String s) {
        super(s);
    }

    public AuthException() {
        super("Ошибка входа.");
    }
}
