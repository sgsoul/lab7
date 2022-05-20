package commands;

import collection.HumanManager;
import common.auth.User;
import common.exceptions.*;
import common.commands.*;


public class RemoveFirstCommand extends CommandImpl {
    private final HumanManager collectionManager;

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
            throw new AuthException("� ��� ��� �������. ������� ��� ������:  " + owner);
        collectionManager.removeFirst();
        return "������� #" + id + " �����.";
    }
}