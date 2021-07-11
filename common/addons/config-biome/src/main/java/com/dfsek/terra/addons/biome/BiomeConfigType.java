package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;

import java.lang.reflect.Type;
import java.util.function.Supplier;

public class BiomeConfigType implements ConfigType<BiomeTemplate, BiomeBuilder> {
    private final ConfigPack pack;
    private final BiomeFactory factory;

    public BiomeConfigType(ConfigPack pack) {
        this.pack = pack;
        this.factory = new BiomeFactory(pack);
    }

    @Override
    public BiomeTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new BiomeTemplate(pack, main);
    }

    @Override
    public ConfigFactory<BiomeTemplate, BiomeBuilder> getFactory() {
        return factory;
    }

    @Override
    public Class<BiomeBuilder> getTypeClass() {
        return BiomeBuilder.class;
    }

    @Override
    public Supplier<OpenRegistry<BiomeBuilder>> registrySupplier() {
        return () -> pack.getRegistryFactory().create(registry -> (TypeLoader<BiomeBuilder>) (t, c, loader) -> {
            if(c.equals("SELF")) return null;
            BiomeBuilder obj = registry.get((String) c);
            if(obj == null)
                throw new LoadException("No such " + t.getTypeName() + " matching \"" + c + "\" was found in this registry.");
            return obj;
        });
    }
}
