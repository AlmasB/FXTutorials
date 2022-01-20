package com.almasb.desktop;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class DirectoryBrowserApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        var listView = new ListView<String>();

        var btn = new Button("Browse...");
        btn.setOnAction(e -> {

            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(Paths.get(System.getProperty("user.dir")).toFile());
            var file = chooser.showDialog(null);

            if (file != null) {
                var startDir = file.toPath();

                try {
                    listView.getItems().clear();

                    Files.walk(startDir)
                            .filter(path -> Files.isDirectory(path))
                            .forEach(dir -> {
                                listView.getItems().add(dir.toString());
                            });

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        var root = new VBox(btn, listView);
        root.setPrefSize(800, 600);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
