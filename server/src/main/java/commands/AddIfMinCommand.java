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
        collectionManager.addIfMin(getHumanArg());
        return ("Добавлен элемент: " + getHumanArg().toString());
    }

}
