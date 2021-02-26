package com.almasb.animations;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class AnimationApp extends Application {

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        NotificationPane pane = new NotificationPane(200, 600);
        pane.setTranslateX(800 - 200);

        Button btn = new Button("Animate");
        btn.setOnAction(e -> {
            pane.animate();
        });

        root.getChildren().addAll(btn, pane);

        return root;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
