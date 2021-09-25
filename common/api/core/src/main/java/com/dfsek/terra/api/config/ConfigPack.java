package com.dfsek.terra.api.config;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.meta.RegistryFactory;
import com.dfsek.terra.api.registry.meta.RegistryHolder;
import com.dfsek.terra.api.tectonic.LoaderHolder;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;


public interface ConfigPack extends LoaderRegistrar, LoaderHolder, RegistryHolder, StringIdentifiable {
    WorldConfig toWorldConfig(World world);
    
    void registerConfigType(ConfigType<?, ?> type, String id, int priority);
    
    Set<TerraAddon> addons();
    
    boolean vanillaMobs();
    
    boolean vanillaStructures();
    
    boolean vanillaCaves();
    
    boolean disableStructures();
    
    boolean doBetaCarvers();
    
    boolean vanillaFlora();
    
    BiomeProvider getBiomeProviderBuilder();
    
    <T> CheckedRegistry<T> getOrCreateRegistry(Type clazz);
    
    default <T> CheckedRegistry<T> getOrCreateRegistry(Class<T> clazz) {
        return getOrCreateRegistry((Type) clazz);
    }
    
    default <T> CheckedRegistry<T> getOrCreateRegistry(TypeKey<T> type) {
        return getOrCreateRegistry(type.getType());
    }
    
    List<GenerationStageProvider> getStages();
    
    Loader getLoader();
    
    String getAuthor();
    
    String getVersion();
    
    Map<String, String> getLocatable();
    
    RegistryFactory getRegistryFactory();
    
    ChunkGeneratorProvider getGeneratorProvider();
}
