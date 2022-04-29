package common.exceptions;

/**
 * выдается, когда аргумент команды недопустим.
 */

public class InvalidCommandArgumentException extends CommandException {
    public InvalidCommandArgumentException(String s) {
        super(s);
    }
}
