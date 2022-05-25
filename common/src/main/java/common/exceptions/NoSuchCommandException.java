package common.exceptions;

/**
 * Выбрасывается, когда пользователь вводит неправильную команду.
 */

public class NoSuchCommandException extends CommandException {
    public NoSuchCommandException(String cmd) {
        super("[NoSuchCommandException] Неправильная команда.");
    }
}