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

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.event.functional.FunctionalEventHandler;


public class YamlAddon implements MonadAddonInitializer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlAddon.class);

    @Override
    public @NotNull Monad<?, Init<?>> initialize() {
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
