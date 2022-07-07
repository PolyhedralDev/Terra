/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.terra.addons.chunkgenerator.config.NoiseChunkGeneratorPackConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.noise.BiomeNoiseConfigTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.noise.BiomeNoiseProperties;
import com.dfsek.terra.addons.chunkgenerator.config.palette.BiomePaletteTemplate;
import com.dfsek.terra.addons.chunkgenerator.config.palette.PaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.config.palette.SlantLayer;
import com.dfsek.terra.addons.chunkgenerator.generation.NoiseChunkGenerator3D;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;


public class NoiseChunkGenerator3DAddon implements AddonInitializer {
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        PropertyKey<PaletteInfo> paletteInfoPropertyKey = Context.create(PaletteInfo.class);
        PropertyKey<BiomeNoiseProperties> noisePropertiesPropertyKey = Context.create(BiomeNoiseProperties.class);
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .priority(1000)
                .then(event -> {
                    NoiseChunkGeneratorPackConfigTemplate config = event.loadTemplate(new NoiseChunkGeneratorPackConfigTemplate());
            
                    event.getPack()
                         .getOrCreateRegistry(ChunkGeneratorProvider.class)
                         .register(addon.key("NOISE_3D"),
                                   pack -> new NoiseChunkGenerator3D(pack, platform, config.getElevationBlend(),
                                                                     config.getHorizontalRes(),
                                                                     config.getVerticalRes(), noisePropertiesPropertyKey,
                                                                     paletteInfoPropertyKey));
                    event.getPack()
                         .applyLoader(SlantLayer.class, SlantLayer::new);
                })
                .failThrough();
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigurationLoadEvent.class)
                .then(event -> {
                    if(event.is(Biome.class)) {
                        event.getLoadedObject(Biome.class).getContext().put(paletteInfoPropertyKey,
                                                                            event.load(new BiomePaletteTemplate(platform)).get());
                        event.getLoadedObject(Biome.class).getContext().put(noisePropertiesPropertyKey,
                                                                            event.load(new BiomeNoiseConfigTemplate()).get());
                    }
                })
                .failThrough();
    }
}
