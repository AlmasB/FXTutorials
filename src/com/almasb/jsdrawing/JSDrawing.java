package com.almasb.jsdrawing;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class JSDrawing extends Application {

    @FXML
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("js_drawing.fxml"));
        loader.setController(this);

        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("JS Drawing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
