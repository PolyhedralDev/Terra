package com.dfsek.terra.addons.biome.query;

import java.util.Collection;

import com.dfsek.terra.addons.biome.query.impl.BiomeTagFlattener;
import com.dfsek.terra.addons.biome.query.impl.BiomeTagHolder;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.world.biome.Biome;


public class BiomeQueryAPIAddon implements AddonInitializer {
    public static PropertyKey<BiomeTagHolder> BIOME_TAG_KEY = Context.create(BiomeTagHolder.class);
    @Inject
    private Platform platform;
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPostLoadEvent.class)
                .then(event -> {
                    Collection<Biome> biomes = event
                            .getPack()
                            .getRegistry(Biome.class)
                            .entries();
            
                    BiomeTagFlattener flattener = new BiomeTagFlattener(biomes
                                                                                .stream()
                                                                                .flatMap(biome -> biome.getTags().stream())
                                                                                .toList());
            
                    biomes.forEach(biome -> biome.getContext().put(BIOME_TAG_KEY, new BiomeTagHolder(biome, flattener)));
                })
                .global();
    }
}
