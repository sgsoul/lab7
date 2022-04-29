package common.exceptions;

import java.nio.charset.StandardCharsets;

/**
 * Исключения с файлами
 */

public class
FileException extends Exception {
    public FileException(String msg) {
        //super(new String(msg.getBytes(), StandardCharsets.UTF_8));
        super(msg);
    }
}