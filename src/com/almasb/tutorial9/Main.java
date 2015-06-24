package com.almasb.tutorial9;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Parent createContent() {
        TextField fieldName = new TextField();
        TextField fieldHP = new TextField();

        Button btnSave = new Button("SAVE");
        btnSave.setOnAction(event -> {
            SaveData data = new SaveData();
            data.name = fieldName.getText();
            data.hp = Integer.parseInt(fieldHP.getText());
            try {
                ResourceManager.save(data, "1.save");
            }
            catch (Exception e) {
                System.out.println("Couldn't save: " + e.getMessage());
            }
        });

        Button btnLoad = new Button("LOAD");
        btnLoad.setOnAction(event -> {
            try {
                SaveData data = (SaveData) ResourceManager.load("1.save");
                fieldName.setText(data.name);
                fieldHP.setText(String.valueOf(data.hp));
            }
            catch (Exception e) {
                System.out.println("Couldn't load save data: " + e.getMessage());
            }
        });

        VBox vbox = new VBox(10, fieldName, fieldHP, btnSave, btnLoad);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(50, 50, 50, 50));
        return vbox;
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
