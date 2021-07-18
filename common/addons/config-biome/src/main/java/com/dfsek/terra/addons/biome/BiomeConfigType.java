package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.TypeToken;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;

import java.util.function.Supplier;

public class BiomeConfigType implements ConfigType<BiomeTemplate, SeededTerraBiome> {
    private final ConfigPack pack;
    private final BiomeFactory factory;

    public static final TypeToken<SeededTerraBiome> BIOME_TYPE_TOKEN = new TypeToken<>() {};

    public BiomeConfigType(ConfigPack pack) {
        this.pack = pack;
        this.factory = new BiomeFactory(pack);
    }

    @Override
    public BiomeTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new BiomeTemplate(pack, main);
    }

    @Override
    public ConfigFactory<BiomeTemplate, SeededTerraBiome> getFactory() {
        return factory;
    }

    @Override
    public TypeToken<SeededTerraBiome> getTypeClass() {
        return BIOME_TYPE_TOKEN;
    }

    @Override
    public Supplier<OpenRegistry<SeededTerraBiome>> registrySupplier() {
        return () -> pack.getRegistryFactory().create(registry -> (TypeLoader<SeededTerraBiome>) (t, c, loader) -> {
            if(c.equals("SELF")) return null;
            SeededTerraBiome obj = registry.get((String) c);
            if(obj == null)
                throw new LoadException("No such " + t.getType().getTypeName() + " matching \"" + c + "\" was found in this registry.");
            return obj;
        });
    }
}
