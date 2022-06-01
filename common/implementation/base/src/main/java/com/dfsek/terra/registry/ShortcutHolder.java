package com.dfsek.terra.registry;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.tectonic.ShortcutLoader;


public class ShortcutHolder<T> implements TypeLoader<T> {
    private final Map<String, ShortcutLoader<T>> shortcuts = new HashMap<>();
    private final Registry<T> back;
    
    public ShortcutHolder(Registry<T> back) {
        this.back = back;
    }
    
    public ShortcutHolder<T> register(String id, ShortcutLoader<T> loader) {
        if(shortcuts.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Attempted to register duplicate shortcut " + id + ", previously registered to " + shortcuts.get(id)
                                                                                                                .getClass()
                                                                                                                .getCanonicalName());
        }
        shortcuts.put(id, loader);
        return this;
    }
    
    @Override
    public T load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader, DepthTracker depthTracker)
    throws LoadException {
        String id = (String) o;
        if(id.contains(":")) {
            String shortcut = id.substring(0, id.indexOf(":"));
            if(shortcuts.containsKey(shortcut)) {
                return shortcuts.get(shortcut).load(configLoader, id.substring(id.indexOf(":") + 1),
                                                    depthTracker.intrinsic("Using shortcut \"" + shortcut + "\""));
            }
            throw new LoadException("Shortcut \"" + shortcut + "\" is not defined.", depthTracker);
        }
        return back.load(annotatedType, o, configLoader, depthTracker);
    }
}
