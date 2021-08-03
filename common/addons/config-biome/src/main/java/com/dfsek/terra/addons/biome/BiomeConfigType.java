package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.function.Supplier;

public class BiomeConfigType implements ConfigType<BiomeTemplate, TerraBiome> {
    public static final TypeKey<TerraBiome> BIOME_TYPE_TOKEN = new TypeKey<>() {
    };
    private final BiomeFactory factory;

    public BiomeConfigType(ConfigPack pack) {
        this.factory = new BiomeFactory(pack);
    }

    @Override
    public BiomeTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new BiomeTemplate(pack, main);
    }

    @Override
    public ConfigFactory<BiomeTemplate, TerraBiome> getFactory() {
        return factory;
    }

    @Override
    public TypeKey<TerraBiome> getTypeKey() {
        return BIOME_TYPE_TOKEN;
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
}
