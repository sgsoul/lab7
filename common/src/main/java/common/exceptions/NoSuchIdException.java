package common.exceptions;

public class NoSuchIdException extends CollectionException {
    public NoSuchIdException(Integer id) {
        super("[NoSuchIdException] Элемент #" + id + " не найден.");
    }
    public NoSuchIdException(String id) {
        super("[NoSuchIdException] Элемент # [" + id + "] не найден.");
    }
}
