package com.dfsek.terra.config.loaders.mod;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.TerraPlugin;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class ModDependentConfigSectionLoader implements TypeLoader<ModDependentConfigSection<?>> {
    private final TerraPlugin main;

    public ModDependentConfigSectionLoader(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ModDependentConfigSection<?> load(Type type, Object c, ConfigLoader loader) throws LoadException {
        if(type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type generic = pType.getActualTypeArguments()[0];

            if(c instanceof Map && ((Map<?, ?>) c).containsKey("default")) {
                Map<String, ?> map = (Map<String, ?>) c;

                ModDependentConfigSection<Object> configSection = new ModDependentConfigSection<>(main, loader.loadType(generic, map.get("default")));

                if(map.containsKey("mods")) {
                    for(Map.Entry<String, ?> modEntry : ((Map<String, ?>) map.get("mods")).entrySet()) {
                        configSection.add(modEntry.getKey(), loader.loadType(generic, modEntry.getValue()));
                    }
                }

                return configSection;
            } else {
                return ModDependentConfigSection.withDefault(loader.loadType(generic, c)); // Load the original type otherwise.
            }
        } else throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type);
    }
}
