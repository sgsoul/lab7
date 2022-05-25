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

    void addCommand(Command cmd);

    /**
     * Запускает команду.
     */
    Response runCommand(Request req);


    Command getCommand(String key);

    default Command getCommand(Request req) {
        return getCommand(req.getCommandName());
    }

    boolean hasCommand(String s);

    default boolean hasCommand(Request req) {
        return hasCommand(req.getCommandName());
    }

    /**
     * Выполнение в консоли.
     */

    void consoleMode();


    /**
     * Выполнение скрипта.
     */

    Response fileMode(String path) throws FileException, InvalidDataException, ConnectionException;

}
