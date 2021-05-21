package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.util.collections.MaterialSet;

import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings("unchecked")
public class MaterialSetLoader implements TypeLoader<MaterialSet> {
    @Override
    public MaterialSet load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        List<String> stringData = (List<String>) o;
        MaterialSet set = new MaterialSet();

        for(String string : stringData) {
            try {
                set.add(configLoader.loadClass(BlockType.class, string));
            } catch(NullPointerException e) {
                throw new LoadException("Invalid data identifier \"" + string + "\"", e);
            }
        }

        return set;
    }
}
