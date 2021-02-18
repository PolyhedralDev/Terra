package com.dfsek.terra.api.core.event;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.core.event.annotations.Priority;
import com.dfsek.terra.api.core.event.events.Cancellable;
import com.dfsek.terra.api.core.event.events.Event;
import com.dfsek.terra.api.util.ReflectionUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerraEventManager implements EventManager {
    private final Map<Class<? extends Event>, List<ListenerHolder>> listeners = new HashMap<>();
    private final TerraPlugin main;

    public TerraEventManager(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public boolean callEvent(Event event) {
        listeners.getOrDefault(event.getClass(), Collections.emptyList()).forEach(listenerHolder -> {
                    try {
                        listenerHolder.method.invoke(listenerHolder.listener, event);
                    } catch(InvocationTargetException e) {
                        StringWriter writer = new StringWriter();
                        e.getTargetException().printStackTrace(new PrintWriter(writer));
                        main.getLogger().warning("Exception occurred during event handling:");
                        main.getLogger().warning(writer.toString());
                        main.getLogger().warning("Report this to the maintainers of " + listenerHolder.method.getName());
                    } catch(Exception e) {
                        StringWriter writer = new StringWriter();
                        e.printStackTrace(new PrintWriter(writer));
                        main.getLogger().warning("Exception occurred during event handling:");
                        main.getLogger().warning(writer.toString());
                        main.getLogger().warning("Report this to the maintainers of " + listenerHolder.method.getName());
                    }
                }
        );
        if(event instanceof Cancellable) return !((Cancellable) event).isCancelled();
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerListener(EventListener listener) {
        Class<? extends EventListener> listenerClass = listener.getClass();
        Method[] methods = ReflectionUtil.getMethods(listenerClass);

        for(Method method : methods) {
            if(method.getParameterCount() != 1) continue; // Check that parameter count is only 1.
            Class<?> eventParam = method.getParameterTypes()[0];
            if(!Event.class.isAssignableFrom(eventParam)) continue; // Check that parameter is an Event.

            Priority p = method.getAnnotation(Priority.class);

            int priority = p == null ? 0 : p.value();

            method.setAccessible(true);

            List<ListenerHolder> holders = listeners.computeIfAbsent((Class<? extends Event>) eventParam, e -> new ArrayList<>());

            holders.add(new ListenerHolder(method, listener, priority));

            holders.sort(Comparator.comparingInt(ListenerHolder::getPriority)); // Sort priorities.
        }
    }

    private static final class ListenerHolder {
        private final Method method;
        private final EventListener listener;
        private final int priority;

        private ListenerHolder(Method method, EventListener listener, int priority) {
            this.method = method;
            this.listener = listener;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }
}
