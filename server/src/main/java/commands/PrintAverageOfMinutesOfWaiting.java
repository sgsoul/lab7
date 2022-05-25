package commands;

import common.collection.HumanManager;
import common.data.HumanBeing;
import common.exceptions.*;
import common.commands.*;

import java.util.OptionalDouble;

public class PrintAverageOfMinutesOfWaiting extends CommandImpl {
    private final HumanManager collectionManager;

    public PrintAverageOfMinutesOfWaiting(HumanManager cm) {
        super("print_average_of_minutes_of_waiting", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        OptionalDouble minutes = collectionManager.getCollection().stream()
                .mapToDouble(HumanBeing::getMinutesOfWaiting)
                .average();
        return "Среднее время ожидания: " + minutes.getAsDouble() + " минут.";
    }
}
