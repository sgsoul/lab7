package common.exceptions;

/**
 * Выбрасывается, когда коллекция пуста
 */

public class EmptyCollectionException extends CommandException {
    public EmptyCollectionException() {
        super("[EmptyCollectionException] Коллекция пуста!");
    }
}
