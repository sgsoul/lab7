package commands;

import common.commands.*;
import common.exceptions.*;

public class ExecuteScriptCommand extends CommandImpl {
    ClientCommandManager commandManager;

    public ExecuteScriptCommand(ClientCommandManager cm) {
        super("execute_script", CommandType.NORMAL);
        commandManager = cm;
    }

    @Override
    public String execute() {
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        if (commandManager.getStack().contains(getStringArg())) throw new RecursiveScriptExecuteException();
        commandManager.getStack().add(getStringArg());
        ClientCommandManager process = new ClientCommandManager(commandManager.getClient());
        try {
            process.fileMode(getStringArg());
        } catch (FileException e) {
            throw new CommandException("Файл не найден.");
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        commandManager.getStack().pop();
        return "Успешно!";
    }
}

