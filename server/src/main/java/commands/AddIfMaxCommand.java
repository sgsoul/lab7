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
        collectionManager.addIfMax(getHumanArg());
        return ("Добавлен элемент: " + getHumanArg().toString());
    }

}
