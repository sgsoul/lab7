package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;

public class AddIfMaxCommand extends CommandImpl {
    private CollectionManager<HumanBeing> collectionManager;

    public AddIfMaxCommand(CollectionManager<HumanBeing> cm) {
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
