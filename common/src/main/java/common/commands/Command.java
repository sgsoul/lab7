package common.commands;

import common.connection.Request;
import common.connection.Response;

/**
 * Интерфейс обратного вызова команды.
 */

public interface Command {
    public Response run();

    public String getName();

    public CommandType getType();

    public void setArgument(Request a);
}