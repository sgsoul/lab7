package commands;

import collection.HumanManager;
import common.auth.User;
import common.exceptions.*;
import common.commands.*;
import database.HumanDBManager;


public class ClearCommand extends CommandImpl {
    private final HumanDBManager collectionManager;

    public ClearCommand(HumanManager cm) {
        super("clear", CommandType.NORMAL);
        collectionManager = (HumanDBManager) cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        User user = getArgument().getUser();
        collectionManager.clear(user);
        return "Коллекция очищена.";
    }

}
