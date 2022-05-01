package commands;

import collection.HumanManager;
import common.auth.User;
import common.exceptions.*;
import common.commands.*;

public class ClearCommand extends CommandImpl {
    private HumanManager collectionManager;

    public ClearCommand(HumanManager cm) {
        super("clear", CommandType.NORMAL);
   //     collectionManager = (HumanDatabaseManager) cm; //todo sgl
    }

    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        User user = getArgument().getUser();
  //      collectionManager.clear(user);
        return "Коллекция очищена.";
    }

}
