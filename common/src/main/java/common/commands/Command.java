package common.commands;

import common.connection.CollectionOperation;
import common.connection.Request;
import common.connection.Response;
import common.exceptions.*;


/**
 * Интерфейс обратного вызова команды.
 */

public interface Command {

    Response run() throws InvalidDataException, CommandException, FileException, ConnectionException, CollectionException;

    String getName();

    CommandType getType();

    void setArgument(Request a);

    CollectionOperation getOperation();
}