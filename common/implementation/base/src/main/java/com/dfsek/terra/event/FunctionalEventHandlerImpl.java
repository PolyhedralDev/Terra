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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.event.functional.EventContext;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class FunctionalEventHandlerImpl implements FunctionalEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(FunctionalEventHandlerImpl.class);
    
    private final Map<Type, List<EventContextImpl<?>>> contextMap = new HashMap<>();
    
    @SuppressWarnings("unchecked")
    @Override
    public void handle(Event event) {
        contextMap.getOrDefault(event.getClass(), Collections.emptyList()).forEach(context -> {
            try {
                if(event instanceof PackEvent) {
                    if((context.isGlobal() || ((PackEvent) event).getPack().addons().containsKey(context.getAddon()))) {
                        ((EventContextImpl<Event>) context).handle(event);
                    }
                } else {
                    ((EventContextImpl<Event>) context).handle(event);
                }
            } catch(Exception e) {
                if(context.isFailThrough() && event instanceof FailThroughEvent)
                    throw e; // Rethrow if it's fail-through.
                // else warn
                logger.warn("Exception occurred during event handling. Report this to the maintainers of {}@{}",
                            context.getAddon().getID(), context.getAddon().getVersion().getFormatted(), e);
            }
        });
    }
    
    @Override
    public <T extends Event> EventContext<T> register(BaseAddon addon, Class<T> clazz) {
        EventContextImpl<T> eventContext = new EventContextImpl<>(addon, clazz, this);
        contextMap.computeIfAbsent(clazz, c -> new ArrayList<>()).add(eventContext);
        return eventContext;
    }
    
    @Override
    public <T extends Event> EventContext<T> register(BaseAddon addon, TypeKey<T> clazz) {
        EventContextImpl<T> eventContext = new EventContextImpl<>(addon, clazz.getType(), this);
        contextMap.computeIfAbsent(clazz.getType(), c -> new ArrayList<>()).add(eventContext);
        return eventContext;
    }
    
    public void recomputePriorities(Type target) {
        contextMap.get(target).sort(Comparator.naturalOrder());
    }
}
