package common.exceptions;

/**
 * Выбрасывается при циклическом вызове скрипта.
 */

public class RecursiveScriptExecuteException extends CommandException {
    public RecursiveScriptExecuteException() {
        super("[RecursiveScriptExecuteException] Попытка выполнения рекурсивного скрипта.");
    }
}
