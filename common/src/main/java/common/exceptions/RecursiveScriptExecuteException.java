package common.exceptions;

/**
 * Выбрасывается при циклическом вызове скрипта.
 */

public class RecursiveScriptExecuteException extends CommandException {
    public RecursiveScriptExecuteException() {
        super("Попытка выполнения рекурсивного скрипта.");
    }
}
