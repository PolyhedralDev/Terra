package com.dfsek.terra.addons.biome.extrusion;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.addons.biome.extrusion.api.ReplaceableBiome;
import com.dfsek.terra.addons.biome.extrusion.config.BiomeExtrusionTemplate;
import com.dfsek.terra.addons.biome.extrusion.config.ReplaceableBiomeLoader;
import com.dfsek.terra.addons.biome.extrusion.config.extrusions.ReplaceExtrusionTemplate;
import com.dfsek.terra.addons.biome.extrusion.config.extrusions.SetExtrusionTemplate;
import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.generic.Construct;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomeExtrusionAddon implements MonadAddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<Extrusion>>> EXTRUSION_REGISTRY_KEY = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<BiomeProvider>>> PROVIDER_REGISTRY_KEY = new TypeKey<>() {
    };
    
    
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((functionalEventHandler, base, platform) -> Init.ofPure(Construct.construct(() -> {
                    functionalEventHandler.register(base, ConfigPackPostLoadEvent.class)
                                          .then(event -> {
                                              Registry<Biome> biomeRegistry = event.getPack().getRegistry(Biome.class);
                                              event.getPack().applyLoader(ReplaceableBiome.class,
                                                                          new ReplaceableBiomeLoader(biomeRegistry));
                                          });
                    return functionalEventHandler.register(base, ConfigPackPreLoadEvent.class)
                                                 .then(event -> {
                                                     Registry<Supplier<ObjectTemplate<BiomeProvider>>> providerRegistry =
                                                             event.getPack()
                                                                  .createRegistry(PROVIDER_REGISTRY_KEY);
                                                     providerRegistry.register(base.key("EXTRUSION"), BiomeExtrusionTemplate::new);
                                                 })
                                                 .then(event -> {
                                                     Registry<Supplier<ObjectTemplate<Extrusion>>> extrusionRegistry =
                                                             event.getPack().createRegistry(
                                                                     EXTRUSION_REGISTRY_KEY);
                                                     extrusionRegistry.register(base.key("SET"), SetExtrusionTemplate::new);
                                                     extrusionRegistry.register(base.key("REPLACE"), ReplaceExtrusionTemplate::new);
                                                 })
                                                 .failThrough();
                }))
                ));
    }
}
