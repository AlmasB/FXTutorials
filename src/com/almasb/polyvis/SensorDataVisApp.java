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

import java.util.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SensorDataVisApp extends Application {

    private List<CanvasLineChart> charts = new ArrayList<>();

    private double t = 0.0;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        Canvas canvas = new Canvas(800, 600);
        GraphicsContext g = canvas.getGraphicsContext2D();

        charts.add(new CanvasLineChart(g, Color.RED, new RandomDataSource()));
        //charts.add(new CanvasLineChart(g, Color.GREEN, new RandomDataSource()));
        //charts.add(new CanvasLineChart(g, Color.BLUE, () -> Math.random() * 0.3));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                t += 0.016;

                if (t > 0.2) {
                    g.clearRect(0, 0, 800, 600);

                    g.setStroke(Color.BLACK);

                    for (int y = 0; y < 600; y += 100) {
                        g.strokeLine(0, y, 800, y);
                    }

                    charts.forEach(CanvasLineChart::update);

                    t = 0.0;
                }
            }
        };
        timer.start();

        root.getChildren().add(canvas);

        return root;
    }

    private static class CanvasLineChart {
        private GraphicsContext g;
        private Color color;
        private DataSource<Double> dataSource;

        private Deque<Double> buffer = new ArrayDeque<>();

        private double oldX = -1;
        private double oldY = -1;


        private static final int PIXELS_PER_UNIT = 10;
        private static final int MAX_ITEMS = 800 / PIXELS_PER_UNIT;

        public CanvasLineChart(GraphicsContext g, Color color, DataSource<Double> dataSource) {
            this.g = g;
            this.color = color;
            this.dataSource = dataSource;
        }

        public void update() {
            double value = dataSource.getValue();

            buffer.addLast(value);

            if (buffer.size() > MAX_ITEMS) {
                buffer.removeFirst();
            }

            // render
            g.setStroke(color);
            g.setLineWidth(2.5);

            buffer.forEach(y -> {
                if (oldY > -1) {
                    // [0..1] * 600 = [0..600]
                    g.strokeLine(
                            oldX * PIXELS_PER_UNIT,
                            oldY * 600,
                            (oldX + 1) * PIXELS_PER_UNIT,
                            y * 600
                    );
                }

                oldX = oldX + 1;
                oldY = y;
            });

            oldX = -1;
            oldY = -1;
        }
    }

    private static class RandomDataSource implements DataSource<Double> {

        private Random random = new Random();

        @Override
        public Double getValue() {
            return random.nextDouble();
        }
    }

    private interface DataSource<T> {
        T getValue();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
