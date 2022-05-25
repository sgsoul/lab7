package common.exceptions;

public class CannotAddException extends CollectionException {
    public CannotAddException() {
        super("[CannotAddException] Не удалось добавить.");
    }
}
