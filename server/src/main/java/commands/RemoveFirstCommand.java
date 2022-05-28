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


public class RemoveFirstCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public RemoveFirstCommand(HumanManager cm) {
        super("remove_first", CommandType.NORMAL, CollectionOperation.REMOVE);
        collectionManager = cm;
    }


    @Override
    public Response run() throws AuthException {
        User user = getArgument().getUser();

        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        HumanBeing human = collectionManager.getCollection().iterator().next();
        int id = human.getId();
        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();
        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new PermissionException(owner);
        collectionManager.removeFirst();
        return new AnswerMsg().info("Элемент #" + id + " удалён.").setCollection(List.of(human));
    }
}