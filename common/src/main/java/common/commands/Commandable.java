package common.commands;

import common.connection.Request;
import common.connection.Response;
import common.exceptions.ConnectionException;
import common.exceptions.FileException;
import common.exceptions.InvalidDataException;

public interface Commandable {

    /**
     * Добавление команды.
     */

    public void addCommand(Command cmd);

    /**
     * Запускает команду.
     */

    public Response runCommand(Request req) throws FileException, InvalidDataException, ConnectionException;

    public Command getCommand(String key);

    public default Command getCommand(Request req) {
        return getCommand(req.getCommandName());
    }

    public boolean hasCommand(String s);

    public default boolean hasCommand(Request req) {
        return hasCommand(req.getCommandName());
    }

    /**
     * Выполнение в консоли.
     */

    public void consoleMode() throws FileException, InvalidDataException, ConnectionException;

    /**
     * Выполнение скрипта.
     */

    public void fileMode(String path) throws FileException, InvalidDataException, ConnectionException;
}
