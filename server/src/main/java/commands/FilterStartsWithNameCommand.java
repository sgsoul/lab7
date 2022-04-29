package commands;

import common.commands.*;

import java.util.List;

import collection.*;
import common.data.*;
import common.exceptions.*;

public class FilterStartsWithNameCommand extends CommandImpl {
    private CollectionManager<HumanBeing> collectionManager;

    public FilterStartsWithNameCommand(CollectionManager<HumanBeing> cm) {
        super("filter_starts_with_name", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        String start = getStringArg();
        List<HumanBeing> list = collectionManager.filterStartsWithName(getStringArg());
        if (list.isEmpty()) return "Ни один из элементов не имеет имени, начинающегося с " + start;
        String s = list.stream()
                .sorted(new HumanBeing.SortingComparator())
                .map(e -> e.toString()).reduce("", (a, b) -> a + b + "\n");
        return s;
    }
}
