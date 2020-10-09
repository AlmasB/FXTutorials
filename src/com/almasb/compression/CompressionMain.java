package com.almasb.compression;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class CompressionMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        StackPane root = new StackPane();

        root.setPrefSize(800, 600);

        Button btn = new Button("Press");
        btn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                try {
                    Compressor compressor = new SimpleCompressor();

                    byte[] original = Files.readAllBytes(file.toPath());

                    byte[] compressed = compressor.compress(original);

                    System.out.println("Original size: " + original.length);
                    System.out.println("Compressed size: " + compressed.length);

                    byte[] decompressed = compressor.decompress(compressed);

                    System.out.println(Arrays.equals(original, decompressed));

                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });

        root.getChildren().add(btn);

        return root;
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
