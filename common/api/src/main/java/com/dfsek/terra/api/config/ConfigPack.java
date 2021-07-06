package com.dfsek.terra.api.config;

import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

import java.util.Map;
import java.util.Set;

public interface ConfigPack extends LoaderRegistrar {
    @SuppressWarnings("unchecked")
    <T> CheckedRegistry<T> getRegistry(Class<T> clazz);

    BiomeProvider.BiomeProviderBuilder getBiomeProviderBuilder();

    WorldConfig toWorldConfig(TerraWorld world);

    CheckedRegistry<ConfigType<?, ?>> getConfigTypeRegistry();

    Set<TerraAddon> addons();

    String getID();

    String getAuthor();

    String getVersion();

    boolean vanillaMobs();

    boolean vanillaStructures();

    boolean vanillaCaves();

    boolean disableStructures();

    Map<String, String> getLocatable();

    boolean doBetaCarvers();

    boolean vanillaFlora();
}
