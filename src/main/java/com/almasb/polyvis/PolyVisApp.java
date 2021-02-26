package com.almasb.polyvis;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class PolyVisApp extends Application {

    private static final int W = 1280;
    private static final int H = 720;

    private static final double PIXELS_PER_UNIT = 40.0;
    private static final double DURATION = 5.0;

    private List<FunctionData> functions = Arrays.asList(
            new FunctionData(Color.RED, x -> x),
            new FunctionData(Color.BLUE, x -> x * x),
            new FunctionData(Color.GREEN, x -> x * x * x),
            new FunctionData(Color.DARKBLUE, x -> Math.sin(x)),
            new FunctionData(Color.DARKGREEN, x -> Math.cos(x)),
            new FunctionData(Color.LIGHTGRAY, x -> 2 + Math.cos(2*x))
    );

    private double t = 0.0;
    private GraphicsContext g;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W, H);

        Canvas canvas = new Canvas(W, H);
        g = canvas.getGraphicsContext2D();
        g.setLineWidth(3.0);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                t += 0.016;

                if (t >= DURATION)
                    t = 0.0;

                onUpdate();
            }
        };
        timer.start();

        root.getChildren().add(canvas);

        return root;
    }

    private void onUpdate() {
        g.clearRect(0, 0, W, H);

        functions.forEach(f -> {

            g.setStroke(f.color);

            int maxDrawX = (int) (W * t / DURATION);

            for (int drawX = 0; drawX < maxDrawX; drawX++) {
                double x = (drawX - W / 2) / PIXELS_PER_UNIT;
                double y = f.function.apply(x);

                double drawY = H - (y * PIXELS_PER_UNIT + H / 2);

                if (!(f.oldX == 0.0 && f.oldY == 0.0)) {
                    g.strokeLine(f.oldX, f.oldY, drawX, drawY);
                }

                f.oldX = drawX;
                f.oldY = drawY;
            }

            f.oldX = 0.0;
            f.oldY = 0.0;
        });
    }

    private static class FunctionData {
        private Color color;
        private Function<Double, Double> function;

        private double oldX = 0.0;
        private double oldY = 0.0;

        public FunctionData(Color color, Function<Double, Double> function) {
            this.color = color;
            this.function = function;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
