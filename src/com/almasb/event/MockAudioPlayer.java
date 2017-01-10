package com.almasb.event;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class MockAudioPlayer implements AudioPlayer {

    public MockAudioPlayer() {
        EventBus eventBus = ServiceLocator.INSTANCE.getService(EventBus.class);

        eventBus.addEventHandler(GameEvent.PLAYER_DIED, event -> {
            playSound("Player Death");
        });
    }

    @Override
    public void playSound(String name) {
        System.out.println("Playing sound: " + name);
    }
}
