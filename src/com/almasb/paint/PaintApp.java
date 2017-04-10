package com.almasb.paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class PaintApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("paint.fxml"))));
        stage.setTitle("Paint App");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
