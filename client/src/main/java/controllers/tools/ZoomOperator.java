package controllers.tools;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class ZoomOperator {
    private Timeline timeline;
    private Bounds bounds;
    private final double maxW = 2000;
    private final double maxH = 2000;

    public ZoomOperator() {
        this.timeline = new Timeline(60);
        bounds = new BoundingBox(0, 0, 0, 0);
    }

    class Position {
        double x, y;
    }

    public void draggable(Node node) {
        Position mouse = new Position();
        node.setOnMousePressed(event -> {
            mouse.x = event.getSceneX();
            mouse.y = event.getSceneY();
            node.setCursor(Cursor.MOVE);
        });

        node.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouse.x;
            double deltaY = event.getSceneY() - mouse.y;
            node.setTranslateX(node.getTranslateX() + deltaX);
            node.setTranslateY(node.getTranslateY() + deltaY);
            mouse.x = event.getSceneX();
            mouse.y = event.getSceneY();
        });

        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> node.setCursor(Cursor.DEFAULT));

    }

    public void zoom(Node node, double factor, double x, double y) {
        // масштаб
        double oldScale = node.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;

        // смещение
        bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));
        // время
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.scaleYProperty(), scale))
        );
        timeline.play();
    }

    public Bounds getBounds() {
        return bounds;
    }
}
