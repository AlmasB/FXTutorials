package com.almasb.alarm;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.time.LocalTime;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class AlarmController {

    @FXML
    private VBox root;

    @FXML
    private ListView<Alarm> listView;

    private boolean darkTheme = true;

    private AlarmModel alarmModel;

    public AlarmController(AlarmModel model) {
        this.alarmModel = model;
    }

    public void initialize() {
        root.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
        listView.setItems(alarmModel.getAlarms());

        alarmModel.start();
    }

    public void onAdd() {
        alarmModel.addAlarm(new Alarm(LocalTime.now().plusSeconds(5)));
    }

    public void onSwitchTheme() {
        darkTheme = !darkTheme;
        root.getStylesheets().setAll(getClass().getResource(darkTheme ? "dark.css" : "light.css").toExternalForm());
    }

    public void onExit() {
        alarmModel.stop();
    }
}
