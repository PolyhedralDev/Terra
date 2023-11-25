/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.yaml;

import com.dfsek.tectonic.yaml.YamlConfiguration;

import com.dfsek.terra.api.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationDiscoveryEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;

import java.io.IOException;
import java.nio.file.Files;


public class YamlAddon implements AddonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlAddon.class);
    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    @Override
    public void initialize() {
        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigurationDiscoveryEvent.class)
            .then(event -> {
                try {
                    FileUtil.filesWithExtension(event.getPack().getPackDirectory(), ".yml")
                        .forEach((key, value) -> {
                            LOGGER.debug("Discovered config {}", key);
                            try {
                                event.register(key, new YamlConfiguration(Files.newInputStream(value), key));
                            } catch(IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .failThrough();
    }
}
