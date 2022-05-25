package commands;

import common.collection.HumanManager;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.exceptions.*;
import common.commands.*;

import java.util.Arrays;


public class AddCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public AddCommand(HumanManager cm) {
        super("add", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public Response run() throws InvalidDataException, CommandException {
        collectionManager.add(getHumanArg());
        return new AnswerMsg().info("Добавлен элемент: " + getHumanArg().toString()).setCollection(Arrays.asList(getHumanArg()));
    }
}
