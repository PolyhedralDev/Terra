package com.dfsek.terra.event;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.event.functional.EventContext;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class FunctionalEventHandlerImpl implements FunctionalEventHandler {
    private final Map<Type, List<EventContextImpl<?>>> contextMap = new HashMap<>();
    
    private final Platform platform;
    
    public FunctionalEventHandlerImpl(Platform platform) {
        this.platform = platform;
    }
    
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
                if(context.isFailThrough() && event instanceof FailThroughEvent) throw e; // Rethrow if it's fail-through.
                StringWriter writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                platform.logger().warning("Exception occurred during event handling:");
                platform.logger().warning(writer.toString());
                platform.logger().warning(
                        "Report this to the maintainers of " + context.getAddon().getID());
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
