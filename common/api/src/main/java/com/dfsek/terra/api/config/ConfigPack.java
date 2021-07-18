package com.dfsek.terra.api.config;

import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.meta.RegistryFactory;
import com.dfsek.terra.api.registry.meta.RegistryHolder;
import com.dfsek.terra.api.tectonic.LoaderHolder;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;
import com.dfsek.terra.api.util.TypeToken;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigPack extends LoaderRegistrar, LoaderHolder, RegistryHolder {
    SeededBuilder<BiomeProvider> getBiomeProviderBuilder();

    <T> CheckedRegistry<T> getOrCreateRegistry(Type clazz);
    default <T> CheckedRegistry<T> getOrCreateRegistry(Class<T> clazz) {
        return getOrCreateRegistry((Type) clazz);
    }

    default <T> CheckedRegistry<T> getOrCreateRegistry(TypeToken<T> type) {
        return getOrCreateRegistry(type.getType());
    }

    WorldConfig toWorldConfig(TerraWorld world);

    List<GenerationStageProvider> getStages();

    void registerConfigType(ConfigType<?, ?> type, String id, int priority);

    Loader getLoader();

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

    RegistryFactory getRegistryFactory();

    ChunkGeneratorProvider getGeneratorProvider();
}
