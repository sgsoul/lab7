package common.commands;

import common.connection.Request;
import common.connection.Response;
import common.exceptions.FileException;

public interface Commandable {

    /**
     * Добавление команды.
     */

    public void addCommand(Command cmd);

    /**
     * Запускает команду.
     */

    public Response runCommand(Request req);

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

    public void consoleMode();

    /**
     * Выполнение скрипта.
     */

    public void fileMode(String path) throws FileException;
}
