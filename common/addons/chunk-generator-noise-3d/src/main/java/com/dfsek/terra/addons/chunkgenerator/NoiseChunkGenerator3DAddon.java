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
import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.generic.Construct;
import com.dfsek.terra.api.world.biome.Biome;


public class NoiseChunkGenerator3DAddon implements MonadAddonInitializer {
    @Override
    public Monad<?, Init<?>> initialize() {
        PropertyKey<PaletteInfo> paletteInfoPropertyKey = Context.create(PaletteInfo.class);
        PropertyKey<BiomeNoiseProperties> noisePropertiesPropertyKey = Context.create(BiomeNoiseProperties.class);
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((handler, base, platform) -> Init.ofPure(Construct.construct(() -> {
                                                          handler.register(base, ConfigPackPreLoadEvent.class)
                                                                 .priority(1000)
                                                                 .then(event -> {
                                                                     NoiseChunkGeneratorPackConfigTemplate config = event.loadTemplate(new NoiseChunkGeneratorPackConfigTemplate());
    
                                                                     event.getPack()
                                                                          .createRegistry(ChunkGeneratorProvider.class)
                                                                          .register(base.key("NOISE_3D"),
                                                                                    pack -> new NoiseChunkGenerator3D(pack, platform, config.getElevationBlend(),
                                                                                                                      config.getHorizontalRes(),
                                                                                                                      config.getVerticalRes(), noisePropertiesPropertyKey,
                                                                                                                      paletteInfoPropertyKey));
                                                                     event.getPack()
                                                                          .applyLoader(SlantLayer.class, SlantLayer::new);
                                                                 })
                                                                 .failThrough();
                return handler.register(base, ConfigurationLoadEvent.class)
                              .then(event -> {
                                  if(event.is(Biome.class)) {
                                      event.getLoadedObject(Biome.class).getContext().put(paletteInfoPropertyKey,
                                                                                          event.load(new BiomePaletteTemplate(platform)).get());
                                      event.getLoadedObject(Biome.class).getContext().put(noisePropertiesPropertyKey,
                                                                                          event.load(new BiomeNoiseConfigTemplate()).get());
                                  }
                              })
                              .failThrough();
                }))));

    }
}
