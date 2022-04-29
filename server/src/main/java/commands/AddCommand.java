package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;

public class AddCommand extends CommandImpl {
    private CollectionManager<HumanBeing> collectionManager;

    public AddCommand(CollectionManager<HumanBeing> cm) {
        super("add", CommandType.NORMAL);
        collectionManager = cm;
    }

    public CollectionManager<HumanBeing> getManager() {
        return collectionManager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException {
        getManager().add(getHumanArg());
        return "Добавлен элемент: " + getHumanArg().toString();
    }
}
