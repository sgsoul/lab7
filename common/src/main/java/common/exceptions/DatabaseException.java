package common.exceptions;

public class DatabaseException extends CollectionException {
    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException() {
        super("Что-то пошло не так с базой данных.");
    }
}
