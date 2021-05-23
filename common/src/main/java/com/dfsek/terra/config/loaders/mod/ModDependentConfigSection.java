package com.dfsek.terra.config.loaders.mod;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.modloader.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ModDependentConfigSection<T> {
    private final TerraPlugin main;
    private final Map<String, T> results = new HashMap<>();
    private final T defaultValue;

    protected ModDependentConfigSection(TerraPlugin main, T defaultValue) {
        this.main = main;
        this.defaultValue = defaultValue;
    }

    public void add(String id, T value) {
        results.put(id, value);
    }

    public static <T1> ModDependentConfigSection<T1> withDefault(T1 defaultValue) {
        return new ModDependentConfigSection<>(null, defaultValue);
    }

    public T get() {
        if(main != null) {
            Set<String> mods = main.getMods().stream().map(Mod::getID).collect(Collectors.toSet());
            for(Map.Entry<String, T> entry : results.entrySet()) {
                if(mods.contains(entry.getKey())) return entry.getValue();
            }
        }
        return defaultValue;
    }
}
