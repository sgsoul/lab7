package common.commands;

import common.connection.Request;
import common.connection.Response;
import common.exceptions.CommandException;
import common.exceptions.ConnectionException;
import common.exceptions.FileException;
import common.exceptions.InvalidDataException;

/**
 * Интерфейс обратного вызова команды.
 */

public interface Command {
    public Response run() throws InvalidDataException, CommandException, FileException, ConnectionException;
;

    public String getName();

    public CommandType getType();

    public void setArgument(Request a);
}