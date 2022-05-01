package commands;

import collection.HumanManager;
import common.exceptions.*;
import common.commands.*;

public class AddIfMinCommand extends CommandImpl {
    private HumanManager collectionManager;

    public AddIfMinCommand(HumanManager cm) {
        super("add_if_min", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        boolean success = collectionManager.addIfMin(getHumanArg());
        if (success) return ("Добавлен элемент: " + getHumanArg().toString());
        else throw new CommandException("Не удалось добавить элемент.");
    }
}
