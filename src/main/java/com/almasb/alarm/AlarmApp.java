package com.almasb.alarm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class AlarmApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));

        AlarmController controller = new AlarmController(new AlarmModel());
        loader.setController(controller);

        stage.setScene(new Scene(loader.load()));
        stage.setOnCloseRequest(e -> controller.onExit());
        stage.setTitle("Alarm");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
