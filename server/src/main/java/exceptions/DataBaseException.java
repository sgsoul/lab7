package exceptions;

public class DataBaseException extends RuntimeException {
    public DataBaseException(String s) {
        super(s);
    }

    public DataBaseException() {
        super("что мы тут ломаем а??");
    }
}