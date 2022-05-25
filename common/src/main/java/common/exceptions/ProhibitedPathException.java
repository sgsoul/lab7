package common.exceptions;

public class ProhibitedPathException extends FileException {
    public ProhibitedPathException() {
        super("Путь к файлу запрещен.");
    }
}
