package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import org.bukkit.Material;

import java.lang.reflect.Type;

public class MaterialLoader implements TypeLoader<Material> {
    @Override
    public Material load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        return Material.matchMaterial((String) o);
    }
}
