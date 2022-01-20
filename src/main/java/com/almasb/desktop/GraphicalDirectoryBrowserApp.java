package com.almasb.desktop;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class GraphicalDirectoryBrowserApp extends Application {
    private HBox hBox = new HBox(15);

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        ScrollPane scrollPane = new ScrollPane(hBox);

        var root = new StackPane(scrollPane);
        root.setPrefSize(800, 600);

        setContents(Paths.get("./"));

        return root;
    }

    private void setContents(Path dir) {
        hBox.getChildren().clear();

        try {
            Files.walk(dir, 1)
                    .filter(path -> Files.isDirectory(path))
                    .forEach(contentDir -> {
                        var view = new DirectoryView(
                                contentDir.getFileName().toString(),
                                contentDir
                        );

                        view.setOnMouseClicked(e -> {
                            setContents(view.directory);
                        });

                        hBox.getChildren().add(view);
                    });

        } catch (IOException e) {
            System.out.println("Can't walk dir: " + dir);
            e.printStackTrace();
        }
    }

    private static class DirectoryView extends VBox {
        private Path directory;

        DirectoryView(String name, Path directory) {
            setSpacing(5);
            setAlignment(Pos.TOP_CENTER);

            this.directory = directory;

            Text text = new Text(name);
            text.setFont(Font.font(24));

            Rectangle rect = new Rectangle(75, 50, Color.LIGHTYELLOW);
            rect.setStroke(Color.BLACK);

            getChildren().addAll(rect, text);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
