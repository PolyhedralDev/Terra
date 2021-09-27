package com.dfsek.terra.addons.biome.single;

import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@Addon("biome-provider-single")
@Author("Terra")
@Version("1.0.0")
public class SingleBiomeProviderAddon extends TerraAddon {
    public static final TypeKey<Supplier<ObjectTemplate<BiomeProvider>>> PROVIDER_REGISTRY_KEY = new TypeKey<>() {
    };
    
    @Inject
    private Platform platform;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> {
                CheckedRegistry<Supplier<ObjectTemplate<BiomeProvider>>> providerRegistry = event.getPack().getOrCreateRegistry(
                        PROVIDER_REGISTRY_KEY);
                providerRegistry.register("SINGLE", SingleBiomeProviderTemplate::new);
            })
                .failThrough();
    }
}
