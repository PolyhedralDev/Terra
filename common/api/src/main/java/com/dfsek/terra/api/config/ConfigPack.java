package com.dfsek.terra.api.config;

import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public interface ConfigPack extends LoaderRegistrar {
    @SuppressWarnings("unchecked")
    <T> CheckedRegistry<T> getRegistry(Class<T> clazz);

    BiomeProvider.BiomeProviderBuilder getBiomeProviderBuilder();

    WorldConfig toWorldConfig(TerraWorld world);

    CheckedRegistry<ConfigType<?, ?>> getConfigTypeRegistry();
}
