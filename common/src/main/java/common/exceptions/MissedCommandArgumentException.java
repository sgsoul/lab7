package common.exceptions;

/**
 * Выбрасывается, когда пользователь не вводит требуемый аргумент команды.
 */

public class MissedCommandArgumentException extends InvalidCommandArgumentException {
    public MissedCommandArgumentException() {
        super("[MissedCommandArgumentException] Неверный аргумент команды.");
    }
}
