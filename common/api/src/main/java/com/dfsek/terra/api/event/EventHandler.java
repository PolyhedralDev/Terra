package com.dfsek.terra.api.event;

import com.dfsek.terra.api.event.events.Event;

public interface EventHandler {
    void handle(Event event);
}
