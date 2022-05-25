package commands;

import common.collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.CollectionOperation;

public class AddIfMinCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public AddIfMinCommand(HumanManager cm) {
        super("add_if_min", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        collectionManager.addIfMin(getHumanArg());
        return ("Добавлен элемент: " + getHumanArg().toString());
    }

}
