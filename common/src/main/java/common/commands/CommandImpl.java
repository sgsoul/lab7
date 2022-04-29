package common.commands;

import common.connection.*;
import common.data.HumanBeing;
import common.exceptions.*;

/**
 * Реализация команд.
 */

public abstract class CommandImpl implements Command {
    private CommandType type;
    private String name;
    private Request arg;

    public CommandImpl(String command_name, CommandType command_type) {
        name = command_name;
        type = command_type;
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

    public abstract String execute() throws InvalidDataException, CommandException, FileException, ConnectionException;

    /**
     * Выполнение -> ответ.
     */

    public Response run() {
        AnswerMsg res = new AnswerMsg();
        try {
            res.info(execute());
        } catch (ExitException e) {
            res.info(e.getMessage());
            res.setStatus(Status.EXIT);
        } catch (InvalidDataException | CommandException | FileException | ConnectionException e) {
            res.error(e.getMessage());
        }
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
}
