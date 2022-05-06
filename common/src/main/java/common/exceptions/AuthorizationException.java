package common.exceptions;

public class AuthorizationException extends ConnectionException{
    public AuthorizationException(String s) {
        super(s);
    }

    public AuthorizationException() {
        super("какая-то фигня при попытке авторизации. проверьте корректность ввденых логина и пароля. если все ок то " +
                "поздравляю - такого юзера у нас нет - отличный шанс чтобы его создать.");
    }
}
