package common.exceptions;

import java.nio.charset.StandardCharsets;

/**
 * Выбрасывается, когда файла не существует.
 */

public class FileNotExistsException extends FileException {
    public FileNotExistsException() {
        super("Файл не найден.");
    }
}
