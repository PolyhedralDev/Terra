package com.dfsek.terra.addons.biome.extrusion;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.addons.biome.extrusion.api.ReplaceableBiome;
import com.dfsek.terra.addons.biome.extrusion.config.BiomeExtrusionTemplate;
import com.dfsek.terra.addons.biome.extrusion.config.ReplaceableBiomeLoader;
import com.dfsek.terra.addons.biome.extrusion.config.extrusions.ReplaceExtrusionTemplate;
import com.dfsek.terra.addons.biome.extrusion.config.extrusions.SetExtrusionTemplate;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomeExtrusionAddon implements AddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<Extrusion>>> EXTRUSION_REGISTRY_KEY = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<BiomeProvider>>> PROVIDER_REGISTRY_KEY = new TypeKey<>() {
    };
    
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
                    CheckedRegistry<Supplier<ObjectTemplate<BiomeProvider>>> providerRegistry =
                            event.getPack()
                                 .getOrCreateRegistry(PROVIDER_REGISTRY_KEY);
                    providerRegistry.register(addon.key("EXTRUSION"), BiomeExtrusionTemplate::new);
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<Extrusion>>> extrusionRegistry = event.getPack().getOrCreateRegistry(
                            EXTRUSION_REGISTRY_KEY);
                    extrusionRegistry.register(addon.key("SET"), SetExtrusionTemplate::new);
                    extrusionRegistry.register(addon.key("REPLACE"), ReplaceExtrusionTemplate::new);
                })
                .failThrough();
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPostLoadEvent.class)
                .then(event -> {
                    Registry<Biome> biomeRegistry = event.getPack().getRegistry(Biome.class);
                    event.getPack().applyLoader(ReplaceableBiome.class, new ReplaceableBiomeLoader(biomeRegistry));
                });
    }
}
