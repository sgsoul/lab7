package common.exceptions;

/**
 * Выбрасывается, когда пользовательский ввод пуст
 */

public class EmptyStringException extends InvalidDataException {
    public EmptyStringException() {
        super("Строка не может быть пустой!");
    }
    public EmptyStringException(String msg) {
        super(msg);
    }
}
