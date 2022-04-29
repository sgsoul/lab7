package common.exceptions;

/**
 * Как же меня уже достали эти команды и их ошибки
 */

public class CommandException extends RuntimeException {
    public CommandException(String message) {
        super(message);
    }
}
