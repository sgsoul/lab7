package commands;

import collection.HumanCollectionManager;
import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;

import java.util.OptionalDouble;

public class PrintAverageOfMinutesOfWaiting extends CommandImpl {
    private CollectionManager<HumanBeing> collectionManager;

    public PrintAverageOfMinutesOfWaiting(CollectionManager<HumanBeing> cm) {
        super("print_average_of_minutes_of_waiting", CommandType.NORMAL);
        collectionManager =cm;
    }


    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        //if (!hasStringArg()) throw new MissedCommandArgumentException();
        OptionalDouble minutes = collectionManager.getCollection().stream()
                .mapToDouble(humanBeing -> humanBeing.getMinutesOfWaiting())
                .average();
        return "Среднее время ожидания: " + minutes.getAsDouble() + " минут.";
    }
}
