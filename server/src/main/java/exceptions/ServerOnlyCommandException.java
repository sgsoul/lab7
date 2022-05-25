package exceptions;

import common.exceptions.CommandException;

/**
 * Выбрасывается, если команда предназначена только для сервера.
 */

public class ServerOnlyCommandException extends CommandException {
    public ServerOnlyCommandException() {
        super("Эта команда предназначена только для сервера.");
    }
}
