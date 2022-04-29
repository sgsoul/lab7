package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;

import static common.utils.Parser.*;

public class UpdateCommand extends CommandImpl {
    private CollectionManager<HumanBeing> collectionManager;

    public UpdateCommand(CollectionManager<HumanBeing> cm) {
        super("update", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg() || !hasHumanArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("ID не найден.");

        boolean success = collectionManager.updateByID(id, getHumanArg());
        if (success) return "Элемент #" + Integer.toString(id) + " обновлён.";
        else throw new CommandException("Не удалось добавить.");
    }

}
