package com.almasb.tutorial5;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private SimpleIntegerProperty target = new SimpleIntegerProperty();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        Dice dice1 = new Dice();
        Dice dice2 = new Dice();

        dice1.setTranslateX(100);
        dice1.setTranslateY(200);

        dice2.setTranslateX(300);
        dice2.setTranslateY(200);

        Button btn = new Button("New target");
        btn.setOnAction(event -> {
            target.set((int)(Math.random() * (Dice.MAX_VALUE * 2 + 1)));
        });

        btn.setTranslateX(400);
        btn.setTranslateY(200);

        SimpleBooleanProperty bool = new SimpleBooleanProperty();
        bool.bind(target.isEqualTo(dice1.valueProperty.add(dice2.valueProperty)));

        Text message = new Text();
        message.textProperty().bind(target.asString().concat(bool.asString()));

        message.setTranslateX(500);
        message.setTranslateY(200);

        root.getChildren().addAll(dice1, dice2, btn, message);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Tutorial");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
