package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import org.bukkit.block.Biome;

import java.lang.reflect.Type;

public class VanillaBiomeLoader implements TypeLoader<Biome> {
    @Override
    public Biome load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        return Biome.valueOf((String) o);
    }
}
