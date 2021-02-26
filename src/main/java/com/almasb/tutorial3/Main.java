package com.almasb.tutorial3;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private Rectangle target = new Rectangle(40, 40);

    private Gun gun = new Gun();

    private int score = 0;

    private Text screenText = new Text("Score: " + score);
    private Text gunInfo = new Text("Bullets: " + gun.getClip().getBullets());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        target.setFill(Color.RED);
        target.setOnMouseClicked(event -> {
            score += 100;
            screenText.setText("Score: " + score);
        });

        screenText.setTranslateX(500);
        screenText.setTranslateY(50);

        gunInfo.setTranslateX(500);
        gunInfo.setTranslateY(100);

        Pane root = new Pane();
        root.setPrefSize(600, 600);
        root.getChildren().addAll(target, screenText, gunInfo);

        Scene scene = new Scene(root);
        scene.setOnMouseClicked(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {
                gun.fire();
            }
            else {
                gun.reload();
            }

            gunInfo.setText("Bullets: " + gun.getClip().getBullets());
        });

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                target.setTranslateX(Math.random() * 560);
                target.setTranslateY(Math.random() * 560);
            });
        }, 0, 1, TimeUnit.SECONDS);

        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}