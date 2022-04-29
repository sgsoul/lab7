package commands;

import collection.CollectionManager;
import common.commands.*;
import common.data.HumanBeing;
import common.exceptions.*;
import common.file.ReaderWriter;

public class SaveCommand extends CommandImpl {
    ReaderWriter fileManager;
    CollectionManager<HumanBeing> collectionManager;

    public SaveCommand(CollectionManager<HumanBeing> cm, ReaderWriter fm) {
        super("save", CommandType.SERVER_ONLY);
        collectionManager = cm;
        fileManager = fm;
    }

    @Override
    public String execute() throws FileException {
        if (hasStringArg()) {
            fileManager.setPath(getStringArg());
        }
        fileManager.write(collectionManager.serializeCollection());
        return "Коллекция успешно сохранена";
    }
}
