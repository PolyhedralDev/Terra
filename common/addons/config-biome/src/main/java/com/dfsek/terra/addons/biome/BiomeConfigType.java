package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeLoader;

import java.util.function.Supplier;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.TerraBiome;


public class BiomeConfigType implements ConfigType<BiomeTemplate, TerraBiome> {
    public static final TypeKey<TerraBiome> BIOME_TYPE_TOKEN = new TypeKey<>() {
    };
    private final BiomeFactory factory;
    
    public BiomeConfigType(ConfigPack pack) {
        this.factory = new BiomeFactory(pack);
    }
    
    @Override
    public Supplier<OpenRegistry<TerraBiome>> registrySupplier(ConfigPack pack) {
        return () -> pack.getRegistryFactory().create(registry -> (TypeLoader<TerraBiome>) (t, c, loader) -> {
            if(c.equals("SELF")) return null;
            TerraBiome obj = registry.get((String) c);
            if(obj == null)
                throw new LoadException("No such " + t.getType().getTypeName() + " matching \"" + c + "\" was found in this registry.");
            return obj;
        });
    }
    
    @Override
    public BiomeTemplate getTemplate(ConfigPack pack, Platform platform) {
        return new BiomeTemplate(pack, platform);
    }
    
    @Override
    public ConfigFactory<BiomeTemplate, TerraBiome> getFactory() {
        return factory;
    }
    
    @Override
    public TypeKey<TerraBiome> getTypeKey() {
        return BIOME_TYPE_TOKEN;
    }
}
