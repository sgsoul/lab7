package commands;

import common.collection.HumanManager;
import common.auth.User;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.exceptions.*;
import common.commands.*;
import database.HumanDBManager;


public class ClearCommand extends CommandImpl {
    private final HumanDBManager collectionManager;

    public ClearCommand(HumanManager cm) {
        super("clear", CommandType.NORMAL, CollectionOperation.REMOVE);
        collectionManager = (HumanDBManager) cm;
    }

    @Override
    public Response run() {
        AnswerMsg answerMsg = new AnswerMsg();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        User user = getArgument().getUser();
        answerMsg.setCollection(collectionManager.clear(user));
        answerMsg.info("Коллекция очищена.");
        return answerMsg;
    }

}
