package com.almasb.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public interface EventBus {
    void fireEvent(Event event);

    <T extends Event> void addEventHandler(EventType<T> type, EventHandler<? super T> handler);
    <T extends Event> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler);
}
