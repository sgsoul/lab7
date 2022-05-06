package common.commands;

import common.connection.*;
import common.data.HumanBeing;
import common.exceptions.*;

/**
 * Реализация команд.
 */

public abstract class CommandImpl implements Command {
    private final CommandType type;
    private final String name;
    private Request arg;

    public CommandImpl(String n, CommandType t) {
        name = n;
        type = t;
    }

    public CommandType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    /**
     * Команда выполнения пользователя.
     */

    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException, CollectionException {
        return "";
    }

    /**
     * Выполнение -> ответ.
     */


    public Response run() throws InvalidDataException, CommandException, FileException, ConnectionException, CollectionException {
        AnswerMsg res = new AnswerMsg();
        res.info(execute());
        return res;
    }

    public Request getArgument() {
        return arg;
    }

    public void setArgument(Request req) {
        arg = req;
    }

    public boolean hasStringArg() {
        return arg != null && arg.getStringArg() != null && !arg.getStringArg().equals("");
    }

    public boolean hasHumanArg() {
        return arg != null && arg.getHuman() != null;
    }

    public String getStringArg() {
        return getArgument().getStringArg();
    }

    public HumanBeing getHumanArg() {
        return getArgument().getHuman();
    }

    public boolean hasUserArg() {
        return arg != null && arg.getUser() != null && arg.getUser().getLogin() != null;
    }
}
