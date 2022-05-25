package common.exceptions;

import java.nio.charset.StandardCharsets;

/**
 * Исключения с файлами
 */

public class FileException extends Exception {
    public FileException(String msg) {
        super(msg);
    }

    public FileException() {
        super("[FileException] не удается прочитать файл.");
    }
}