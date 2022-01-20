package com.almasb.explore;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class CanvasApp extends Application {

    private static final double MAX_ATTRACT_DISTANCE = 350;
    private static final double MIN_ATTRACT_DISTANCE = 0.1;
    private static final double FORCE_CONSTANT = 5000;

    private double mouseX;
    private double mouseY;

    private GraphicsContext g;

    private List<Particle> particles = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        var scene = new Scene(createContent());
        scene.setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });

        stage.setScene(scene);
        stage.show();
    }

    private Parent createContent() {
        for (int y = 0; y < 720 / 10; y++) {
            for (int x = 0; x < 1280 / 10; x++) {
                particles.add(new Particle(x * 10, y * 10, Color.BLUE));
            }
        }

        var canvas = new Canvas(1280, 720);
        g = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        var pane = new Pane(canvas);
        pane.setPrefSize(1280, 720);
        return pane;
    }

    private void onUpdate() {
        g.clearRect(0, 0, 1280, 720);

        var cursorPos = new Point2D(mouseX, mouseY);

        particles.forEach(p -> {
            p.update(cursorPos);

            g.setFill(p.color);

            g.fillOval(p.x - 1, p.y - 1, 2, 2);
        });
    }

    private static class Particle {
        double x;
        double y;
        Color color;

        double originalX;
        double originalY;

        Particle(double x, double y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;

            originalX = x;
            originalY = y;
        }

        void update(Point2D cursorPos) {
            var distance = cursorPos.distance(x, y);

            if (distance > MAX_ATTRACT_DISTANCE) {
                x = originalX;
                y = originalY;
            } else if (distance < MIN_ATTRACT_DISTANCE) {
                x = cursorPos.getX();
                y = cursorPos.getY();
            } else {
                var vector = cursorPos.subtract(x, y);
                var scaledLength = FORCE_CONSTANT * 1 / distance;
                vector = vector.normalize().multiply(scaledLength);

                x = originalX + vector.getX();
                y = originalY + vector.getY();

                // C * 1 / d

                // * ----> x
                // * -->   x
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
