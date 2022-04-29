package common.exceptions;

/**
 * Выбрасывается, когда пользователь не вводит требуемый аргумент команды.
 */

public class MissedCommandArgumentException extends InvalidCommandArgumentException {
    public MissedCommandArgumentException() {
        super("Неверный аргумент команды.");
    }
}
