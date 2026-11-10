package com.dfsek.terra.addons.attribute.minecraft;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.registry.key.RegistryKey;


public record MobSpawnSettings(Map<String, List<Spawner>> spawnsByCategory, Map<RegistryKey, Cost> spawnCosts) {
    public static final MobSpawnSettings EMPTY = new MobSpawnSettings(Map.of(), Map.of());

    public MobSpawnSettings {
        Map<String, List<Spawner>> copy = new LinkedHashMap<>();
        spawnsByCategory.forEach((k, v) -> copy.put(k, List.copyOf(v)));
        spawnsByCategory = Collections.unmodifiableMap(copy);
        spawnCosts = Collections.unmodifiableMap(new LinkedHashMap<>(spawnCosts));
    }

    public record Spawner(RegistryKey type, int weight, IntProviderValue count) { }


    public record Cost(double energyBudget, double charge) { }
}
