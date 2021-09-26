package com.dfsek.terra.registry.config;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;
import com.dfsek.terra.registry.OpenRegistryImpl;


public class ConfigTypeRegistry extends OpenRegistryImpl<ConfigType<?, ?>> {
    private final BiConsumer<String, ConfigType<?, ?>> callback;
    
    private final Platform main;
    
    public ConfigTypeRegistry(Platform main, BiConsumer<String, ConfigType<?, ?>> callback) {
        super(new LinkedHashMap<>()); // Ordered
        this.callback = callback;
        this.main = main;
    }
    
    @Override
    public boolean register(String identifier, Entry<ConfigType<?, ?>> value) {
        callback.accept(identifier, value.getValue());
        main.getDebugLogger().info("Registered config registry with ID " + identifier + " to type " +
                                   ReflectionUtil.typeToString(value.getValue().getTypeKey().getType()));
        return super.register(identifier, value);
    }
}
