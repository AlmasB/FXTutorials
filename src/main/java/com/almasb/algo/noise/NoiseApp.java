package com.almasb.algo.noise;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NoiseApp extends Application {

    private Parent createContent() {
        Pane root = new Pane();

        Image original = new Image(getClass().getResource("screen.jpg").toExternalForm());
        Image modified = applyNoise(original);

        ImageView imageView = new ImageView(modified);

        root.getChildren().add(new HBox(imageView));

        return root;
    }

    private Image applyNoise(Image source) {
        PixelReader pixelReader = source.getPixelReader();

        int w = (int) source.getWidth();
        int h = (int) source.getHeight();

        WritableImage image = new WritableImage(w, h);
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color color = pixelReader.getColor(x, y);

                double noise = Math.random() / 3;

                Color newColor = new Color(
                        Math.min(color.getRed() + noise, 1),
                        Math.min(color.getGreen() + noise, 1),
                        Math.min(color.getBlue() + noise, 1),
                        1);

                pixelWriter.setColor(x, y, newColor);
            }
        }

        return image;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Noise App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
