package commands;

import collection.HumanManager;
import common.auth.User;
import common.exceptions.*;
import common.commands.*;

import static common.utils.Parser.*;

public class RemoveByIdCommand extends CommandImpl {
    private HumanManager collectionManager;

    public RemoveByIdCommand(HumanManager cm) {
        super("remove_by_id", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id))
            throw new InvalidCommandArgumentException("Не найден ID #" + id);

        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();

        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new AuthException("У вас нет доступа, элемент был создан " + owner);
        collectionManager.removeByID(id);
        return "Элемент #" + id + " удалён.";
    }

}
