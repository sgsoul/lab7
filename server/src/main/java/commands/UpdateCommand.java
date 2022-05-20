package commands;

import collection.HumanManager;
import common.auth.User;
import common.exceptions.*;
import common.commands.*;

import static common.utils.Parser.*;

public class UpdateCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public UpdateCommand(HumanManager cm) {
        super("update", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (hasStringArg() || !hasHumanArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("ID �� ������.");
        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();
        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new AuthException("��� ����. ������� ������: " + owner);

        collectionManager.updateByID(id, getHumanArg());
        return "������� #" + id + " �������.";
    }

}
