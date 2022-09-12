package com.example.rg_d2;

import javafx.event.Event;
import javafx.event.EventType;

public class SelectionEvent extends Event {
    public final EventType<SelectionEvent> ANY = new EventType<>("ANY");
    public final EventType<SelectionEvent> SELECTED_BACKGROUND = new EventType<>("SELECTED_BACKGROUND");
    public final EventType<SelectionEvent> SELECTED_CANNON = new EventType<>("SELECTED_CANNON");

    public SelectionEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
