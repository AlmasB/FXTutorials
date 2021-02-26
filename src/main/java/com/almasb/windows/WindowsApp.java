package com.almasb.windows;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WindowsApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Point2D center = new Point2D(1920 / 2, 1080 / 2);

        //firstTry(center);

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                StageNode node = new StageNode(
                        x * 200,
                        y * 200,
                        200, 200
                );

                TranslateTransition tt = new TranslateTransition(
                        Duration.seconds(0.66), node);
                tt.setFromY(node.stage.getY() - 60);
                tt.setToY(node.stage.getY());
                tt.setAutoReverse(true);
                tt.setCycleCount(1000);
                tt.setDelay(Duration.seconds(0.25 * x));
                tt.play();
            }
        }
    }

    private void firstTry(Point2D center) {
        for (int angle = 0; angle < 360; angle += 30) {
            Point2D vector = new Point2D(
                    Math.cos(Math.toRadians(angle)),
                    Math.sin(Math.toRadians(angle))
            );

            Point2D newPoint = center.add(vector.multiply(300));
            Point2D topLeft = newPoint.subtract(100, 100);

            StageNode node = new StageNode(
                    (int)topLeft.getX(),
                    (int) topLeft.getY(),
                    200, 200
            );

            TranslateTransition tt = new TranslateTransition(Duration.seconds(2), node);
            tt.setFromX(node.stage.getX());
            tt.setFromY(node.stage.getY());
            tt.setToX(center.getX());
            tt.setToY(center.getY());
            tt.setAutoReverse(true);
            tt.setCycleCount(100);
            tt.setDelay(Duration.seconds(0.5));
            tt.play();
        }
    }

    public static class StageNode extends Parent {
        private Stage stage;

        StageNode(int x, int y, int w, int h) {
            getChildren().add(new Rectangle(200, 200, Color.BLUE));

            this.stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setX(x);
            stage.setY(y);
            stage.setWidth(w);
            stage.setHeight(h);
            stage.setScene(new Scene(this));
            stage.setOnCloseRequest(e -> Platform.exit());

            translateXProperty().addListener((o, old, newX) -> {
                stage.setX(newX.intValue());
            });

            translateYProperty().addListener((o, old, newY) -> {
                stage.setY(newY.intValue());
            });

            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
