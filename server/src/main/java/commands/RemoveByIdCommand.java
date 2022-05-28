package commands;

import common.collection.HumanManager;
import common.auth.User;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.data.HumanBeing;
import common.exceptions.*;
import common.commands.*;


import java.util.List;

import static common.utils.Parser.*;

public class RemoveByIdCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public RemoveByIdCommand(HumanManager cm) {
        super("remove_by_id", CommandType.NORMAL, CollectionOperation.REMOVE);
        collectionManager = cm;

    }


    @Override
    public Response run() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (hasStringArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id))
            throw new NoSuchIdException(id);
        String owner = collectionManager.getByID(id).getUserLogin();
        String humanCreatorLogin = user.getLogin();

        if (humanCreatorLogin == null || !humanCreatorLogin.equals(owner))
            throw new PermissionException(owner);
        HumanBeing human = collectionManager.getByID(id);
        collectionManager.removeByID(id) ;
        return new AnswerMsg().info( "Элемент #" + id + " удалён.").setCollection(List.of(human));
    }

}
