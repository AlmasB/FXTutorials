package com.almasb.nativewindow;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class NativeWindowApp extends Application {

    private int y = 0;
    private NativeWindow window;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setX(100);
        stage.setY(100);
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        window = new NativeWindow(100 + 800, 100, 800, 600);
        window.show();

        Pane root = new Pane();
        root.setPrefSize(800, 600);

        Canvas canvas = new Canvas(800, 600);
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.fillRect(0, 0, 800, 600);
        g.setFill(Color.WHITE);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (y == 600)
                    return;

                if (y % 2 == 0) {
                    g.fillRect(0, y, 800, 1);
                } else {
                    window.fillRect(0, y, 800, 1);
                }

                y++;
            }
        };

        timer.start();

        root.getChildren().add(canvas);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
