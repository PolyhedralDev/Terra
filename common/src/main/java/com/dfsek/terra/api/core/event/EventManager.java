package com.dfsek.terra.api.core.event;

public interface EventManager {
    void callEvent(Event event);

    void registerListener(EventListener listener);
}
