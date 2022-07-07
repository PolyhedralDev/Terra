package com.dfsek.terra.addons.biome.extrusion;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomeExtrusionProvider implements BiomeProvider {
    private final BiomeProvider delegate;
    private final Set<Biome> biomes;
    private final List<Extrusion> extrusions;
    private final int resolution;
    
    public BiomeExtrusionProvider(BiomeProvider delegate, List<Extrusion> extrusions, int resolution) {
        this.delegate = delegate;
        this.biomes = delegate.stream().collect(Collectors.toSet());
        extrusions.forEach(e -> biomes.addAll(e.getBiomes()));
        this.extrusions = extrusions;
        this.resolution = resolution;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        Biome delegated = delegate.getBiome(x, y, z, seed);
        
        return extrude(delegated, x, y, z, seed);
    }
    
    public Biome extrude(Biome original, int x, int y, int z, long seed) {
        for(Extrusion extrusion : extrusions) {
            original = extrusion.extrude(original, x, y, z, seed);
        }
        return original;
    }
    
    @Override
    public Column<Biome> getColumn(int x, int z, long seed, int min, int max) {
        return delegate.getBaseBiome(x, z, seed)
                       .map(base -> (Column<Biome>) new BaseBiomeColumn(this, base, min, max, x, z, seed))
                       .orElseGet(() -> BiomeProvider.super.getColumn(x, z, seed, min, max));
    }
    
    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return delegate.getBaseBiome(x, z, seed);
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return biomes;
    }
    
    @Override
    public int resolution() {
        return resolution;
    }
    
    public BiomeProvider getDelegate() {
        return delegate;
    }
}
