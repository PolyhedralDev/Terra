package com.dfsek.terra.addons.biome.extrusion;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class BiomeExtrusionProvider implements BiomeProvider {
    private final BiomeProvider delegate;
    private final Set<Biome> biomes;
    private final List<Extrusion> extrusions;
    private final int resolution;
    
    public BiomeExtrusionProvider(BiomeProvider delegate, List<Extrusion> extrusions, int resolution) {
        this.delegate = delegate;
        this.biomes = delegate.stream().collect(Collectors.toSet());
        this.extrusions = extrusions;
        this.resolution = resolution;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        Biome delegated = delegate.getBiome(x, y, z, seed);
    
        for(Extrusion extrusion : extrusions) {
            delegated = extrusion.extrude(delegated, x, y, z, seed);
        }
        
        return delegated;
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return biomes;
    }
    
    public int getResolution() {
        return resolution;
    }
    
    @Override
    public Column<Biome> getColumn(int x, int z, WorldProperties properties) {
        return new ExtrusionColumn(properties, this, x, z);
    }
}
