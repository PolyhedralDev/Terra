package com.dfsek.terra.api.core.event;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.util.ReflectionUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerraEventManager implements EventManager {
    private final Map<Class<? extends Event>, Map<EventListener, List<Method>>> listeners = new HashMap<>();
    private final TerraPlugin main;

    public TerraEventManager(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public void callEvent(Event event) {
        if(!listeners.containsKey(event.getClass())) return;
        listeners.get(event.getClass()).forEach((eventListener, methods) -> methods.forEach(method -> {
                    try {
                        method.invoke(eventListener, event);
                    } catch(InvocationTargetException e) {
                        StringWriter writer = new StringWriter();
                        e.getTargetException().printStackTrace(new PrintWriter(writer));
                        main.getLogger().warning("Exception occurred during event handling:");
                        main.getLogger().warning(writer.toString());
                        main.getLogger().warning("Report this to the maintainers of " + eventListener.getClass().getCanonicalName());
                    } catch(Exception e) {
                        StringWriter writer = new StringWriter();
                        e.printStackTrace(new PrintWriter(writer));
                        main.getLogger().warning("Exception occurred during event handling:");
                        main.getLogger().warning(writer.toString());
                        main.getLogger().warning("Report this to the maintainers of " + eventListener.getClass().getCanonicalName());
                    }
                })
        );
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
            method.setAccessible(true);
            listeners.computeIfAbsent((Class<? extends Event>) eventParam, e -> new HashMap<>()).computeIfAbsent(listener, l -> new ArrayList<>()).add(method);
        }
    }
}
