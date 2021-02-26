package com.almasb.algo.disintegration;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisintegrationApp extends Application {

    private double time = 0;

    private GraphicsContext g;

    private List<Particle> particles = new ArrayList<>();
    private int fullSize;

    private Parent createContent() {
        Pane root = new Pane();

        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);

        g = canvas.getGraphicsContext2D();

        Image image = new Image(getClass().getResource("armor.png").toExternalForm());
        g.drawImage(image, 700, 50);

        disintegrate(image);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;

                if (time > 2)
                    update();
            }
        };
        timer.start();

//        Timeline timeline = new Timeline();
//        timeline.setDelay(Duration.seconds(2));
//        timeline.setCycleCount(5);
//        timeline.setAutoReverse(true);
//
//        List<KeyValue> values = new ArrayList<>();
//
//        particles.forEach(p -> {
//            values.add(new KeyValue(p.xProperty(), p.getX() - 700 + 100, Interpolator.DISCRETE));
//        });
//
//        Collections.shuffle(values);
//
//        int chunkSize = 50;
//        int chunks = values.size() / chunkSize + 1;
//
//        for (int i = 0; i < chunks; i++) {
//            timeline.getKeyFrames().add(
//                    new KeyFrame(Duration.seconds(Math.random() * 3),
//                            values.subList(i * chunkSize, i == chunks - 1 ? values.size() : (i+1) * chunkSize).toArray(new KeyValue[0]))
//            );
//        }
//
//        timeline.play();

        return root;
    }

    private void disintegrate(Image image) {
        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);

                if (!color.equals(Color.TRANSPARENT)) {
                    Particle p = new Particle(x + 700, y + 50, color);
                    particles.add(p);
                }
            }
        }

        fullSize = particles.size();
    }

    private void update() {
        g.clearRect(0, 0, 1280, 720);

        particles.removeIf(Particle::isDead);

        particles.parallelStream()
                .filter(p -> !p.isActive())
                .sorted((p1, p2) -> (int)(p2.getY() - p1.getY()))
                .limit(fullSize / 60 / 2)
                .forEach(p -> p.activate(new Point2D(Math.random() - 0.5, Math.random() - 0.5).multiply(-1)));

        particles.forEach(p -> {
            p.update();
            p.draw(g);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Disintegration App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
