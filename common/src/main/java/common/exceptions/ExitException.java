package common.exceptions;

/**
 * Выбрасывается при прерывании программы
 */

public class ExitException extends CommandException {
    public ExitException() {
        super("Завершение программы...");
    }
}
