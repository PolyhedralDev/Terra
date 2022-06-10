package com.dfsek.terra.addons.biome.extrusion;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class BiomeExtrusionProvider implements BiomeProvider {
    private final BiomeProvider delegate;
    private final Set<Biome> biomes;
    
    public BiomeExtrusionProvider(BiomeProvider delegate) {
        this.delegate = delegate;
        this.biomes = delegate.stream().collect(Collectors.toSet());
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return delegate.getBiome(x, y, z, seed);
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return biomes;
    }
}
