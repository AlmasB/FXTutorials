package com.almasb.fallout4menu;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Fallout4MenuApp extends Application {

    private Parent createContent() {
        Pane root = new Pane();

        ImageView imageView = new ImageView(new Image(getClass()
                .getResource("res/Fallout4_bg.jpg").toExternalForm()));

        imageView.setFitWidth(1280);
        imageView.setFitHeight(720);

        SepiaTone tone = new SepiaTone(0.85);
        imageView.setEffect(tone);

        Rectangle masker = new Rectangle(1280, 720);
        masker.setOpacity(0);
        masker.setMouseTransparent(true);

        MenuBox menuBox = new MenuBox(250, 350);
        menuBox.setTranslateX(250);
        menuBox.setTranslateY(230);

        MenuBox menuBox2 = new MenuBox(510, 350);
        menuBox2.setTranslateX(250 + 20 + 250);
        menuBox2.setTranslateY(230);

        menuBox.addItem(new MenuItem("CONTINUE", 250));

        MenuItem itemNew = new MenuItem("NEW", 250);
        itemNew.setOnAction(() -> {
            FadeTransition ft = new FadeTransition(Duration.seconds(1.5), masker);
            ft.setToValue(1);

            ft.setOnFinished(e -> {
                root.getChildren().setAll(new LoadingScreen(1280, 720, () -> {
                    masker.setOpacity(0);
                    root.getChildren().setAll(imageView, menuBox, menuBox2, masker);
                }));
            });

            ft.play();
        });
        menuBox.addItem(itemNew);
        menuBox.addItem(new MenuItem("LOAD", 250));

        MenuItem itemSettings = new MenuItem("SETTINGS", 250);
        itemSettings.setOnAction(() -> {
            menuBox2.addItems(
                    new MenuItem("GAMEPLAY", 510),
                    new MenuItem("CONTROLS", 510),
                    new MenuItem("DISPLAY", 510),
                    new MenuItem("AUDIO", 510));
        });

        menuBox.addItem(itemSettings);
        menuBox.addItem(new MenuItem("CREW", 250));

        MenuItem itemExit = new MenuItem("EXIT", 250);
        itemExit.setOnAction(() -> System.exit(0));
        menuBox.addItem(itemExit);

        root.getChildren().addAll(imageView, menuBox, menuBox2, masker);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Fallout4 Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
