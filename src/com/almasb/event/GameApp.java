package com.almasb.event;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class GameApp extends Application {

    private EventBus eventBus;

    private AudioPlayer audioPlayer;

    @Override
    public void start(Stage stage) throws Exception {
        eventBus = ServiceLocator.INSTANCE.getService(EventBus.class);
        audioPlayer = ServiceLocator.INSTANCE.getService(AudioPlayer.class);

        Scene scene = new Scene(new VBox(), 600, 600);
        scene.setOnMouseClicked(event -> {
            eventBus.fireEvent(new GameEvent(GameEvent.PLAYER_DIED));
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ServiceLocator.INSTANCE.registerService(EventBus.class, EventBusProvider.class);
        ServiceLocator.INSTANCE.registerService(AudioPlayer.class, MockAudioPlayer.class);

        launch(args);
    }
}
