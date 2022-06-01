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

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.event.EventHandler;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;


public class EventManagerImpl implements EventManager {
    private final Map<Class<?>, EventHandler> handlers = new HashMap<>();
    
    public EventManagerImpl() {
        registerHandler(FunctionalEventHandler.class, new FunctionalEventHandlerImpl()); // default handler
    }
    
    @Override
    public <T extends Event> T callEvent(T event) {
        handlers.values().forEach(handler -> handler.handle(event));
        return event;
    }
    
    @Override
    public <T extends EventHandler> void registerHandler(Class<T> clazz, T handler) {
        handlers.put(clazz, handler);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends EventHandler> T getHandler(Class<T> clazz) {
        return (T) handlers.computeIfAbsent(clazz, c -> {
            throw new IllegalArgumentException("No event handler registered for class " + clazz.getCanonicalName());
        });
    }
}
