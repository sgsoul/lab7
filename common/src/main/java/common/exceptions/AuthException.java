package common.exceptions;

public class AuthException extends ConnectionException {
    public AuthException() {
        super("[AuthException]", "Такого пользователя не существует.");
    }
    public AuthException(String t, String s){
        super(t,s);
    }
}
