package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.generation.items.ores.Ore;

import java.lang.reflect.Type;

public class OreTypeLoader implements TypeLoader<Ore.Type> {
    @Override
    public Ore.Type load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        return Ore.Type.valueOf((String) o);
    }
}
