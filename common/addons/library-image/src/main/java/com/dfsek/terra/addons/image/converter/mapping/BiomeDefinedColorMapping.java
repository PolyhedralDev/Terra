package com.dfsek.terra.addons.image.converter.mapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.Biome;


public class BiomeDefinedColorMapping<T> implements ColorMapping<T> {
    
    Registry<Biome> biomeRegistry;
    
    Function<Biome, T> converter;
    
    public BiomeDefinedColorMapping(Registry<Biome> biomeRegistry, Function<Biome, T> converter) {
        this.biomeRegistry = biomeRegistry;
        this.converter = converter;
    }
    
    @Override
    public Map<Integer, T> get() {
        Map<Biome, Integer> colorMap = new HashSet<>(biomeRegistry.entries()).stream().collect(Collectors.toMap(b -> b, Biome::getColor));
        Map<Integer, Biome> output = new HashMap<>();
        colorMap.forEach(((biome, color) -> {
            if(!output.containsKey(color)) {
                output.put(color, biome);
            } else {
                throw new IllegalArgumentException(String.format("Biome %s has same color as %s: %x", biome.getID(), output.get(color).getID(), color));
            }
        }));
        return output.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> converter.apply(e.getValue())));
    }
}
