package com.dfsek.terra.addons.biome.query;

import java.util.Collection;

import com.dfsek.terra.addons.biome.query.impl.BiomeTagFlattener;
import com.dfsek.terra.addons.biome.query.impl.BiomeTagHolder;
import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.world.biome.Biome;


public class BiomeQueryAPIAddon implements MonadAddonInitializer {
    public static PropertyKey<BiomeTagHolder> BIOME_TAG_KEY = Context.create(BiomeTagHolder.class);
    
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                ((functionalEventHandler, base) -> Init.ofPure(
                        functionalEventHandler.register(base, ConfigPackPostLoadEvent.class)
                                              .then(event -> {
                                                  Collection<Biome> biomes = event
                                                          .getPack()
                                                          .getRegistry(Biome.class)
                                                          .entries();
                            
                                                  BiomeTagFlattener flattener = new BiomeTagFlattener(
                                                          biomes.stream()
                                                                .flatMap(biome -> biome.getTags().stream())
                                                                .toList());
                            
                                                  biomes.forEach(biome -> biome.getContext()
                                                                               .put(BIOME_TAG_KEY, new BiomeTagHolder(biome, flattener)));
                                              })
                                              .global()
                                                                        )));
    }
}
