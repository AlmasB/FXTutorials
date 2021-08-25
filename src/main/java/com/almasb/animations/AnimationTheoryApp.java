package com.almasb.animations;

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
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class AnimationTheoryApp extends Application {

    private static final int NUM_POINTS = 3600;
    private static final double SIZE = 8;

    private List<AnimatedPoint> points = new ArrayList<>();

    private GraphicsContext g;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        var root = new Pane();
        root.setPrefSize(800, 600);

        var canvas = new Canvas(800, 600);
        g = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        // populate with points

        var degreesPerPoint = 360.0 / NUM_POINTS;

        for (int i = 0; i < NUM_POINTS; i++) {
            var angle = degreesPerPoint * i;

            var vx = Math.cos(Math.toRadians(angle)) * 150;
            var vy = Math.sin(Math.toRadians(angle)) * 150;

            var start = new Point2D(vx + 400, vy + 300);
            var end = new Point2D(vx * 0.25 + 400, vy * 0.25 + 300);

            points.add(new AnimatedPoint(start, end, Duration.seconds(3 + i * 0.001)));
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    private void onUpdate() {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, 800, 600);

        g.setFill(Color.WHITE);

        points.forEach(p -> {
            p.update();

            g.fillOval(p.x - SIZE / 2.0, p.y - SIZE / 2.0, SIZE, SIZE);
        });
    }

    public static class AnimatedPoint {
        private Point2D start;
        private Point2D end;

        private double x;
        private double y;

        private double durationSeconds;
        private double t = 0.0;

        public AnimatedPoint(Point2D start, Point2D end, Duration duration) {
            this.start = start;
            this.end = end;
            this.durationSeconds = duration.toSeconds();

            this.x = start.getX();
            this.y = start.getY();
        }

        public void update() {
            t += 0.016;

            var tRatio = t / durationSeconds;

            if (tRatio >= 1.0) {
                tRatio = 1.0;
            }

            // tRatio  0 .. 0.5 .. 1.0
            // value   0 .. 100 .. 200
            // value   100 .. 150 .. 200

            var progress = interpolate(tRatio);

            x = progress * (end.getX() - start.getX()) + start.getX();
            y = progress * (end.getY() - start.getY()) + start.getY();
        }

        private double interpolate(double ratio) {
            return 1 - ratio;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
