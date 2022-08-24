/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.image;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class ImageBiomeProviderAddon implements MonadAddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<BiomeProvider>>> PROVIDER_REGISTRY_KEY = new TypeKey<>() {
    };
    
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((functionalEventHandler, base, platform) -> Init.ofPure(
                        functionalEventHandler.register(base, ConfigPackPreLoadEvent.class)
                                              .then(event -> {
                                                  CheckedRegistry<Supplier<ObjectTemplate<BiomeProvider>>> providerRegistry = event.getPack().getOrCreateRegistry(
                                                          PROVIDER_REGISTRY_KEY);
                                                  providerRegistry.register(base.key("IMAGE"),
                                                                            () -> new ImageProviderTemplate(event.getPack().getRegistry(Biome.class)));
                                              })
                                              .failThrough()))
                      );
    
    }
}
