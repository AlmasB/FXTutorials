package com.almasb.event;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class GameEvent extends Event {

    public static final EventType<GameEvent> ANY
            = new EventType<>(Event.ANY, "GAME_EVENT");

    public static final EventType<GameEvent> PLAYER_DIED
            = new EventType<>(ANY, "PLAYER_DIED");

    public GameEvent(EventType<GameEvent> type) {
        super(type);
    }
}
