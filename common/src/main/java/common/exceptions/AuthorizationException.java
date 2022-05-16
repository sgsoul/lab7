package common.exceptions;

public class AuthorizationException extends ConnectionException{
    public AuthorizationException(String s) {
        super(s);
    }

    public AuthorizationException() {
        super("Какая-то фигня при попытке авторизации. проверьте корректность веденных логина и пароля. если все ок то " +
                "поздравляю - такого юзера у нас нет - отличный шанс чтобы его создать.");
    }
}
