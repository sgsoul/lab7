package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;

import static common.utils.Parser.*;

public class RemoveByIdCommand extends CommandImpl {
    private CollectionManager<HumanBeing> collectionManager;

    public RemoveByIdCommand(CollectionManager<HumanBeing> cm) {
        super("remove_by_id", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("ID не найден.");

        boolean success = collectionManager.removeByID(id);
        if (success) return "Элемент #" + Integer.toString(id) + " удалён.";
        else throw new CommandException("Не удалось добавить элемент.");
    }

}
