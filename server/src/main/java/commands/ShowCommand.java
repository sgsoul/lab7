package commands;

import common.collection.HumanManager;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.exceptions.*;
import common.commands.*;

public class ShowCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public ShowCommand(HumanManager cm) {
        super("show", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public Response run() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.sort();
        return new AnswerMsg().info(collectionManager.serializeCollection()).setCollection(collectionManager.getCollection()).setStatus(Response.Status.COLLECTION);
    }
}
