package com.almasb.alarm;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class AlarmModel {

    private ObservableList<Alarm> alarms = FXCollections.observableArrayList();
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public ObservableList<Alarm> getAlarms() {
        return FXCollections.unmodifiableObservableList(alarms);
    }

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }

    public void removeAlarm(Alarm alarm) {
        alarms.remove(alarm);
    }

    public void start() {
        executorService.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        executorService.shutdownNow();
    }

    private void tick() {
        Platform.runLater(() -> {
            alarms.stream()
                    .filter(a -> LocalTime.now().isAfter(a.getTime()))
                    .forEach(Alarm::report);

            alarms.removeIf(a -> !a.isActive());
        });
    }
}
