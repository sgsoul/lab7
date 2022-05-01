package commands;

import collection.HumanManager;
import common.exceptions.*;
import common.commands.*;

import java.util.OptionalDouble;

public class PrintAverageOfMinutesOfWaiting extends CommandImpl {
    private HumanManager collectionManager;

    public PrintAverageOfMinutesOfWaiting(HumanManager cm) {
        super("print_average_of_minutes_of_waiting", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        OptionalDouble minutes = collectionManager.getCollection().stream()
                .mapToDouble(humanBeing -> humanBeing.getMinutesOfWaiting())
                .average();
        return "Среднее время ожидания: " + minutes.getAsDouble() + " минут.";
    }
}
