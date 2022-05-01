package commands;

import collection.HumanManager;
import common.auth.User;
import common.exceptions.*;
import common.commands.*;
import common.data.*;

import static common.utils.Parser.*;

public class UpdateCommand extends CommandImpl {
    private HumanManager collectionManager;

    public UpdateCommand(HumanManager cm) {
        super("update", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg() || !hasHumanArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("ID не найден.");
        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();
        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new AuthException("Нет прав. Элемент создан: " + owner);

        collectionManager.updateByID(id, getHumanArg());
        return "Элемент #" + id + " обновлён.";
    }

}
