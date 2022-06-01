/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.api.config.Configuration;

import java.util.function.BiConsumer;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.Loader;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.events.PackEvent;


/**
 * Fired when a pack is searched for {@link Configuration}s.
 * <p>
 * Addons should listen to this event if they wish to add
 * another configuration format.
 */
public class ConfigurationDiscoveryEvent implements PackEvent, FailThroughEvent {
    private final ConfigPack pack;
    private final Loader loader;
    
    private final BiConsumer<String, Configuration> consumer;
    
    public ConfigurationDiscoveryEvent(ConfigPack pack, Loader loader, BiConsumer<String, Configuration> consumer) {
        this.pack = pack;
        this.loader = loader;
        this.consumer = consumer;
    }
    
    public void register(String identifier, Configuration config) {
        consumer.accept(identifier, config);
    }
    
    @Override
    public ConfigPack getPack() {
        return pack;
    }
    
    public Loader getLoader() {
        return loader;
    }
}
