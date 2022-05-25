package commands;


import common.collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.EmptyCollectionException;

import java.util.List;

public class PrintUniqueImpactSpeedCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public PrintUniqueImpactSpeedCommand(HumanManager cm) {
        super("print_unique_impact_speed", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        List<Integer> list = collectionManager.getUniqueImpactSpeed();
        return list.stream().map(n -> Integer.toString(n)).reduce("", (a, b) -> a + b + "\n");
    }
}

