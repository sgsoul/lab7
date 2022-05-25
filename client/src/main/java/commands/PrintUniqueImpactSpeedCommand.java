package commands;


import common.collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.EmptyCollectionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class PrintUniqueImpactSpeedCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public PrintUniqueImpactSpeedCommand(HumanManager cm) {
        super("print_unique_impact_speed", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        ObservableList<Integer> list = collectionManager.getUniqueImpactSpeed().stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
        Platform.runLater(() -> {
            ListView<Integer> listView = new ListView<>();
            listView.setItems(list);
            Stage stage = new Stage();
            Scene scene = new Scene(listView);
            stage.setScene(scene);
            stage.show();
        });
        return list.stream().map(n -> Integer.toString(n)).reduce("", (a, b) -> a + b + "\n");
    }
}