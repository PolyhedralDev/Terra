package com.dfsek.terra.registry.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.registry.OpenRegistryImpl;

import java.lang.reflect.Type;

public class BiomeRegistry extends OpenRegistryImpl<BiomeBuilder> {
    @Override
    public BiomeBuilder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        if(o.equals("SELF")) return null;
        BiomeBuilder biome = get((String) o);
        if(biome == null)
            throw new LoadException("No such " + type.getTypeName() + " matching \"" + o + "\" was found in this registry.");
        return biome;
    }
}
