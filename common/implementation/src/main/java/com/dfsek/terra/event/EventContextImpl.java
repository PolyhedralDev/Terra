package com.dfsek.terra.event;

import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.event.functional.EventContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventContextImpl<T extends Event> implements EventContext<T>, Comparable<EventContextImpl<T>> {
    private final List<Consumer<T>> actions = new ArrayList<>();
    private int priority;
    private boolean failThrough = false;
    private boolean global = false;

    private final TerraAddon addon;

    private final Class<? extends Event> eventClass;

    public EventContextImpl(TerraAddon addon, Class<? extends Event> eventClass) {
        this.addon = addon;
        this.eventClass = eventClass;
    }

    public void handle(T event) {
        actions.forEach(action -> action.accept(event));
    }

    @Override
    public EventContext<T> then(Consumer<T> action) {
        actions.add(action);
        return this;
    }

    @Override
    public EventContext<T> priority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public EventContext<T> failThrough() {
        if(!FailThroughEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalStateException("Cannot fail-through on event which does not implement FailThroughEvent: " + eventClass.getCanonicalName());
        }
        this.failThrough = true;
        return this;
    }

    @Override
    public EventContext<T> global() {
        this.global = true;
        return this;
    }

    @Override
    public int compareTo(@NotNull EventContextImpl<T> o) {
        return this.priority - o.priority;
    }

    public boolean isGlobal() {
        return global;
    }

    public int getPriority() {
        return priority;
    }

    public TerraAddon getAddon() {
        return addon;
    }

    public boolean isFailThrough() {
        return failThrough;
    }
}
