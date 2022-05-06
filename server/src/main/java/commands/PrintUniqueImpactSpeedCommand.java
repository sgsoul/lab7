package commands;


import common.commands.*;

import java.util.List;

import collection.*;
import common.data.*;
import common.exceptions.*;

public class PrintUniqueImpactSpeedCommand extends CommandImpl {
    private HumanManager collectionManager;

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

