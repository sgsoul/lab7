package io;

import common.io.OutputManager;
import controllers.tools.ObservableResourceFactory;
import controllers.tools.ResourceException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class OutputterUI implements OutputManager {

    private ObservableResourceFactory resourceFactory;

    public OutputterUI(ObservableResourceFactory rf) {
        resourceFactory = rf;
    }

    public void error(String s) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String msg = resourceFactory.getString(s);
            if (msg != null) {
                alert.setContentText(msg);
                alert.setHeaderText(null);
            } else {
                alert.setHeaderText(resourceFactory.getString("Внутренняя ошибка."));
                alert.setContentText(s);
            }
            alert.getDialogPane().setStyle("-fx-font-size: 13");
            alert.show();

        });
    }

    public void info(String s) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(resourceFactory.getString(s));
                alert.getDialogPane().setStyle("-fx-font-size: 13");
                alert.setHeaderText(null);
                alert.show();
            } catch (ResourceException ignored) {
            }
        });
    }
}
