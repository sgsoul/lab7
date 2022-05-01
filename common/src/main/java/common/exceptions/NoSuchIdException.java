package common.exceptions;

public class NoSuchIdException extends CollectionException {
    public NoSuchIdException(Integer id) {
        super("Элемент #" + id + " не найден.");
    }
}
