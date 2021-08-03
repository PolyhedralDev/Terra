package com.dfsek.terra.api.event.functional;

import com.dfsek.terra.api.event.events.Event;

import java.util.function.Consumer;

public interface EventContext<T extends Event> {
    EventContext<T> then(Consumer<T> action);

    EventContext<T> priority(int priority);

    EventContext<T> failThrough();

    EventContext<T> global();
}
