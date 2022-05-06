package common.exceptions;

public class EndOfInputException extends RuntimeException {
    public EndOfInputException() {
        super("Конец ввода.");
    }
}
