package commands;

import collection.HumanObservableManager;
import common.collection.HumanManager;
import common.commands.*;

import java.util.List;

import common.data.*;
import common.exceptions.*;
import controllers.MainWindowController;
import javafx.application.Platform;

public class FilterStartsWithNameCommand extends CommandImpl {
    private final HumanObservableManager collectionManager;

    public FilterStartsWithNameCommand(HumanObservableManager cm) {
        super("filter_starts_with_name", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (hasStringArg()) throw new MissedCommandArgumentException();
        String start = getStringArg();
        List<HumanBeing> list = collectionManager.filterStartsWithName(getStringArg());
        MainWindowController controller = collectionManager.getController();
        Platform.runLater(()->{
            controller.getFilter().filter(controller.getNameColumn(),
                    "^" + getStringArg()+".*",
                    HumanBeing::getName);
        });
        if (list.isEmpty()) return "Ни один из элементов не имеет имени, начинающегося с " + start;
        return list.stream()
                .sorted(new HumanBeing.SortingComparator())
                .map(HumanBeing::toString).reduce("", (a, b) -> a + b + "\n");
    }
}