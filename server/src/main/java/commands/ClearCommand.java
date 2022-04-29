package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;

public class ClearCommand extends CommandImpl {
    private CollectionManager<HumanBeing> collectionManager;

    public ClearCommand(CollectionManager<HumanBeing> cm) {
        super("clear", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.clear();
        return "Коллекция очищена.";
    }

}
