package com.almasb.painter;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class AutoPainterApp extends Application {

    private List<Pixel> pixels = new ArrayList<>();
    private int pixelIndex = 0;

    private Image image;
    private GraphicsContext g;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 500);

        image = new Image(getClass().getResource("placeholder.jpg").toExternalForm());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                pixels.add(new Pixel(x, y, image.getPixelReader().getColor(x, y)));
            }
        }

        pixels.sort(Comparator.comparingDouble(p -> {
            double a = new Point2D(100, 200).distance(p.x, p.y);
            double b = new Point2D(500, 450).distance(p.x, p.y);
            double c = new Point2D(300, 339).distance(p.x, p.y);

            return Math.min(Math.min(a, b), c);
        }));

        Canvas canvas = new Canvas(800, 500);
        g = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

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
        int numPixels = 2000;

        while (pixelIndex < pixels.size() && numPixels > 0) {
            Pixel p = pixels.get(pixelIndex);
            Color c = p.color;

            g.setFill(c);
            g.fillRect(p.x, p.y, 1, 1);

            numPixels--;
            pixelIndex++;
        }
    }

    private static class Pixel {
        int x;
        int y;
        Color color;

        Pixel(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
