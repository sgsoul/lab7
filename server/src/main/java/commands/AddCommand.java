package commands;

import collection.HumanManager;
import common.exceptions.*;
import common.commands.*;


public class AddCommand extends CommandImpl {
    private HumanManager collectionManager;

    public AddCommand(HumanManager cm) {
        super("add", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException {
        collectionManager.add(getHumanArg());
        return "Добавлен элемент: " + getHumanArg().toString();
    }
}
