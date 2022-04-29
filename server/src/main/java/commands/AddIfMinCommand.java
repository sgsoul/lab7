package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;

public class AddIfMinCommand extends CommandImpl {
    private CollectionManager<HumanBeing> collectionManager;

    public AddIfMinCommand(CollectionManager<HumanBeing> cm) {
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
