/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.yaml;

import com.dfsek.tectonic.yaml.YamlConfiguration;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;

import com.dfsek.terra.api.util.function.monad.Monad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationDiscoveryEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


public class YamlAddon implements MonadAddonInitializer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlAddon.class);

    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                ((handler, base) -> Init.ofPure(
                        handler.register(base, ConfigurationDiscoveryEvent.class)
                               .then(event -> event.getLoader().open("", ".yml").thenEntries(entries -> entries.forEach(entry -> {
                                   LOGGER.debug("Discovered config {}", entry.getKey());
                                   event.register(entry.getKey(), new YamlConfiguration(entry.getValue(), entry.getKey()));
                               })).close())
                               .failThrough()))
                      );
    }
}
