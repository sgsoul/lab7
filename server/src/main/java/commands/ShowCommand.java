package commands;

import collection.HumanManager;
import common.exceptions.*;
import common.commands.*;

public class ShowCommand extends CommandImpl {
    private HumanManager collectionManager;

    public ShowCommand(HumanManager cm) {
        super("show", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.sort();
        return collectionManager.serializeCollection();
    }

}
