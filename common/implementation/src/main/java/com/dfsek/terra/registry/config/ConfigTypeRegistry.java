package com.dfsek.terra.registry.config;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.util.ReflectionUtil;
import com.dfsek.terra.registry.OpenRegistryImpl;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class ConfigTypeRegistry extends OpenRegistryImpl<ConfigType<?, ?>> {
    private final BiConsumer<String, ConfigType<?, ?>> callback;

    private final TerraPlugin main;

    public ConfigTypeRegistry(TerraPlugin main, BiConsumer<String, ConfigType<?, ?>> callback) {
        super(new LinkedHashMap<>()); // Ordered
        this.callback = callback;
        this.main = main;
    }

    @Override
    public boolean register(String identifier, Entry<ConfigType<?, ?>> value) {
        callback.accept(identifier, value.getValue());
        main.getDebugLogger().info("Registered config registry with ID " + identifier + " to type " + ReflectionUtil.typeToString(value.getValue().getTypeClass().getType()));
        return super.register(identifier, value);
    }
}
