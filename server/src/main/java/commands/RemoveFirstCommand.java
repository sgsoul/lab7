package commands;

import collection.HumanManager;
import common.auth.User;
import common.exceptions.*;
import common.commands.*;


public class RemoveFirstCommand extends CommandImpl {
    private HumanManager collectionManager;

    public RemoveFirstCommand(HumanManager cm) {
        super("remove_first", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();

        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        int id = collectionManager.getCollection().iterator().next().getId();
        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();
        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new AuthException("У вас нет доступа. Элемент был создан:  " + owner);
        collectionManager.removeFirst();
        return "Элемент #" + id + " удалён.";
    }
}