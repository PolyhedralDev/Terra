package com.dfsek.terra.registry.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;
import com.dfsek.terra.registry.OpenRegistryImpl;


public class ConfigTypeRegistry extends OpenRegistryImpl<ConfigType<?, ?>> {
    private static final Logger logger = LoggerFactory.getLogger(ConfigTypeRegistry.class);
    
    private final BiConsumer<String, ConfigType<?, ?>> callback;
    
    private final Platform platform;
    
    public ConfigTypeRegistry(Platform platform, BiConsumer<String, ConfigType<?, ?>> callback) {
        super(new LinkedHashMap<>()); // Ordered
        this.callback = callback;
        this.platform = platform;
    }
    
    @Override
    public boolean register(String identifier, Entry<ConfigType<?, ?>> value) {
        callback.accept(identifier, value.getValue());
        logger.debug("Registered config registry with ID {} to type {}", identifier,
                     ReflectionUtil.typeToString(value.getValue().getTypeKey().getType()));
        return super.register(identifier, value);
    }
}
