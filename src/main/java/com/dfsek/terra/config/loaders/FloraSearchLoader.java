package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.generation.items.flora.TerraFlora;

import java.lang.reflect.Type;

public class FloraSearchLoader implements TypeLoader<TerraFlora.Search> {
    @Override
    public TerraFlora.Search load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        return TerraFlora.Search.valueOf((String) o);
    }
}
