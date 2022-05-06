package commands;

import collection.HumanManager;
import common.exceptions.*;
import common.commands.*;

public class InfoCommand extends CommandImpl {
    private HumanManager collectionManager;

    public InfoCommand(HumanManager cm) {
        super("info", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        return collectionManager.getInfo();
    }

}
