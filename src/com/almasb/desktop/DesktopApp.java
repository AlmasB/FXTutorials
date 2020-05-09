package com.almasb.desktop;

import com.almasb.calc.App;
import com.almasb.image.ImageEditingApp;
import com.almasb.paint.PaintApp;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class DesktopApp extends Application {

    private List<DesktopIcon> icons = Arrays.asList(
            new DesktopIcon("Paint", PaintApp.class),
            new DesktopIcon("Calculator", App.class),
            new DesktopIcon("Image Editor", ImageEditingApp.class)
    );

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(1920, 1080);

        VBox boxIcons = new VBox(25);

        icons.forEach(icon -> {
            icon.setOnMouseClicked(e -> {
                try {
                    Application app = icon.appClass.newInstance();

                    Stage stage = new Stage();
                    app.start(stage);
                    stage.close();

                    Parent appRoot = stage.getScene().getRoot();
                    stage.getScene().setRoot(new Pane());

                    DesktopWindow window = new DesktopWindow(appRoot);

                    root.getChildren().add(window);

                } catch (Exception error) {
                    System.out.println("Error: " + error);
                    error.printStackTrace();
                }
            });

            boxIcons.getChildren().add(icon);
        });

        root.getChildren().add(boxIcons);

        return root;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.setFullScreen(true);
        stage.show();
    }

    private static class DesktopIcon extends StackPane {
        String name;
        Class<? extends Application> appClass;

        DesktopIcon(String name, Class<? extends Application> appClass) {
            this.name = name;
            this.appClass = appClass;

            Rectangle bg = new Rectangle(100, 100, null);
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(2.5);

            Text text = new Text(name);

            getChildren().addAll(bg, text);
        }
    }

    private static class DesktopWindow extends Region {
        private double offsetX;
        private double offsetY;

        DesktopWindow(Parent root) {
            setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));

            root.setTranslateY(15);

            setOnMousePressed(e -> {
                offsetX = e.getX();
                offsetY = e.getY();
            });

            setOnMouseDragged(e -> {
                if (e.getButton() != MouseButton.PRIMARY)
                    return;

                setTranslateX(e.getSceneX() - offsetX);
                setTranslateY(e.getSceneY() - offsetY);
            });

            getChildren().add(root);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
