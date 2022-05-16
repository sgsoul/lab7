package commands;

import collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;

public class AddIfMaxCommand extends CommandImpl {
    private final HumanManager collectionManager;

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
