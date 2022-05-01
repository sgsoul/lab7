package commands;

import collection.HumanManager;
import common.exceptions.*;
import common.commands.*;

public class AddIfMaxCommand extends CommandImpl {
    private HumanManager collectionManager;

    public AddIfMaxCommand(HumanManager cm) {
        super("add_if_max", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        boolean success = collectionManager.addIfMax(getHumanArg());
        if (success) return ("Добавлен элемент: " + getHumanArg().toString());
        else throw new CommandException("Не удалось добавить.");
    }

}
