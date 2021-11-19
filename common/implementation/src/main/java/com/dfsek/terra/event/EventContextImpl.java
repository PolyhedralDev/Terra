/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.event;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.functional.EventContext;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;


public class EventContextImpl<T extends Event> implements EventContext<T>, Comparable<EventContextImpl<?>> {
    private final List<Consumer<T>> actions = new ArrayList<>();
    private final BaseAddon addon;
    private final Type eventType;
    private final FunctionalEventHandlerImpl parent;
    private int priority;
    private boolean failThrough = false;
    private boolean global = false;
    
    public EventContextImpl(BaseAddon addon, Type eventType, FunctionalEventHandlerImpl parent) {
        this.addon = addon;
        this.eventType = eventType;
        this.parent = parent;
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
        parent.recomputePriorities(eventType);
        return this;
    }
    
    @Override
    public EventContext<T> failThrough() {
        if(!FailThroughEvent.class.isAssignableFrom(ReflectionUtil.getRawType(eventType))) {
            throw new IllegalStateException(
                    "Cannot fail-through on event which does not implement FailThroughEvent: " + ReflectionUtil.typeToString(eventType));
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
    public int compareTo(@NotNull EventContextImpl<?> o) {
        return this.priority - o.priority;
    }
    
    public boolean isGlobal() {
        return global;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public BaseAddon getAddon() {
        return addon;
    }
    
    public boolean isFailThrough() {
        return failThrough;
    }
}
