package commands;

import collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;

public class AddIfMinCommand extends CommandImpl {
    private final HumanManager collectionManager;

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
