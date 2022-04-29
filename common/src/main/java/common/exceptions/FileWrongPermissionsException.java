package common.exceptions;

/**
 * Выбрасывается, когда недостаточно разрешения для доступа к файлу.
 */

public class FileWrongPermissionsException extends FileException {
    public FileWrongPermissionsException(String s) {
        super(s);
    }
}