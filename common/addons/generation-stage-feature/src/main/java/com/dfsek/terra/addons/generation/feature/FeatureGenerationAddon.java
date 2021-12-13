/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.generation.feature;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.generation.feature.config.BiomeFeaturesTemplate;
import com.dfsek.terra.addons.generation.feature.config.FeatureStageTemplate;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;

import java.util.function.Supplier;


public class FeatureGenerationAddon implements AddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<GenerationStage>>> STAGE_TYPE_KEY = new TypeKey<>() {};
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .then(event -> event.getPack()
                                    .getOrCreateRegistry(STAGE_TYPE_KEY)
                                    .register("FEATURE", () -> new FeatureStageTemplate(platform)))
                .failThrough();
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigurationLoadEvent.class)
                .then(event -> {
                    if(event.is(Biome.class)) {
                        event.getLoadedObject(Biome.class).getContext().put(event.load(new BiomeFeaturesTemplate()).get());
                    }
                })
                .failThrough();
    }
}
