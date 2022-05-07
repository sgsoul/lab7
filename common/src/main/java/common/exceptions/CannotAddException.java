package common.exceptions;

public class CannotAddException extends CollectionException {
    public CannotAddException() {
        super("Не удалось добавить.");
    }
}
