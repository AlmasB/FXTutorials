package com.almasb.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class EventBusProvider implements EventBus {

    private Group group = new Group();

    @Override
    public void fireEvent(Event event) {
        System.out.println("Firing event: " + event);
        group.fireEvent(event);
    }

    @Override
    public <T extends Event> void addEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        group.addEventHandler(type, handler);
    }

    @Override
    public <T extends Event> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        group.removeEventHandler(type, handler);
    }
}
