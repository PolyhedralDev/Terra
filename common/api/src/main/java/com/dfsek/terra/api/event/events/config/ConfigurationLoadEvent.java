package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.Loader;
import com.dfsek.terra.api.event.events.PackEvent;

import java.util.function.Consumer;

public class ConfigurationLoadEvent implements PackEvent {
    private final ConfigPack pack;
    private final Loader loader;

    private final Consumer<Configuration> consumer;

    public ConfigurationLoadEvent(ConfigPack pack, Loader loader, Consumer<Configuration> consumer) {
        this.pack = pack;
        this.loader = loader;
        this.consumer = consumer;
    }

    @Override
    public ConfigPack getPack() {
        return pack;
    }

    public Loader getLoader() {
        return loader;
    }

    public void register(Configuration config) {
        consumer.accept(config);
    }
}
