package common.exceptions;

/**
 * Выбрасывается, когда не удается создать файл
 */

public class CannotCreateFileException extends FileException {
    public CannotCreateFileException() {
        super("Не удается создать файл.");
    }
}
