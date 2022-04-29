package common.exceptions;

/**
 * Выбрасывается, когда аргументы, переданные командной строкой, являются недопустимыми.
 */

public class InvalidProgramArgumentsException extends InvalidDataException {
    public InvalidProgramArgumentsException(String s) {
        super(s);
    }
}
