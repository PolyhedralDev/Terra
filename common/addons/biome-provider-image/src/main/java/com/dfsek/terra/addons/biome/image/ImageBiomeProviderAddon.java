/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.image;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class ImageBiomeProviderAddon implements AddonInitializer {

    public static final TypeKey<Supplier<ObjectTemplate<BiomeProvider>>> PROVIDER_REGISTRY_KEY = new TypeKey<>() {
    };
    private static final Logger logger = LoggerFactory.getLogger(ImageBiomeProviderAddon.class);
    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    @Override
    public void initialize() {
        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigPackPreLoadEvent.class)
            .then(event -> {
                CheckedRegistry<Supplier<ObjectTemplate<BiomeProvider>>> providerRegistry = event.getPack().getOrCreateRegistry(
                    PROVIDER_REGISTRY_KEY);
                providerRegistry.register(addon.key("IMAGE"),
                    () -> new ImageProviderTemplate(event.getPack().getRegistry(Biome.class)));
            })
            .failThrough();
        if(platform.getTerraConfig().isDebugLog())
            logger.warn(
                "The biome-provider-image addon is deprecated and scheduled for removal in Terra 7.0. It is recommended to use the " +
                "biome-provider-image-v2 addon for future pack development instead.");
    }
}
