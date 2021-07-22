package com.dfsek.terra.event;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.event.functional.EventContext;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.reflection.TypeKey;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionalEventHandlerImpl implements FunctionalEventHandler {
    private final Map<Type, List<EventContextImpl<?>>> contextMap = new HashMap<>();

    private final TerraPlugin main;

    public FunctionalEventHandlerImpl(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(Event event) {
        contextMap.getOrDefault(event.getClass(), Collections.emptyList()).forEach(context -> {
            try {
                if(event instanceof PackEvent) {
                    if((context.isGlobal() || ((PackEvent) event).getPack().addons().contains(context.getAddon()))) {
                        ((EventContextImpl<Event>) context).handle(event);
                    }
                } else {
                    ((EventContextImpl<Event>) context).handle(event);
                }
            } catch(Exception e) {
                if(context.isFailThrough()) throw e; // Rethrow if it's fail-through.
                StringWriter writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                main.logger().warning("Exception occurred during event handling:");
                main.logger().warning(writer.toString());
                main.logger().warning("Report this to the maintainers of " + context.getAddon().getName() + ", " + context.getAddon().getAuthor());
            }
        });
    }

    @Override
    public <T extends Event> EventContext<T> register(TerraAddon addon, Class<T> clazz) {
        EventContextImpl<T> eventContext = new EventContextImpl<>(addon);
        contextMap.computeIfAbsent(clazz, c -> new ArrayList<>()).add(eventContext);
        return eventContext;
    }

    @Override
    public <T extends Event> EventContext<T> register(TerraAddon addon, TypeKey<T> clazz) {
        EventContextImpl<T> eventContext = new EventContextImpl<>(addon);
        contextMap.computeIfAbsent(clazz.getType(), c -> new ArrayList<>()).add(eventContext);
        return eventContext;
    }
}
