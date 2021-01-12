package com.dfsek.terra.biome.pipeline;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.biome.pipeline.expand.BiomeExpander;
import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.biome.pipeline.source.BiomeSource;

public class TerraBiomeHolder implements BiomeHolder {
    private final Position original;
    private int width;
    private Biome[][] biomes;

    public TerraBiomeHolder(int width, Position original) {
        this.width = width;
        biomes = new Biome[width][width];
        this.original = original;
    }

    @Override
    public void expand(BiomeExpander expander) {
        Biome[][] old = biomes;
        int oldWidth = width;

        width = 2 * width - 1;

        biomes = new Biome[width][width];
        for(int x = 0; x < oldWidth; x++) {
            for(int z = 0; z < oldWidth; z++) {
                biomes[x * 2][z * 2] = old[x][z];
                if(z != oldWidth - 1) biomes[x * 2][z * 2 + 1] = expander.getBetween(new Position(x, z + 1), old[x][z], old[x][z + 1]);
                if(x != oldWidth - 1) biomes[x * 2 + 1][z * 2] = expander.getBetween(new Position(x + 1, z), old[x][z], old[x + 1][z]);
                if(x != oldWidth - 1 && z != oldWidth - 1)
                    biomes[x * 2 + 1][z * 2 + 1] = expander.getBetween(new Position(x + 1, z + 1), old[x][z], old[x + 1][z + 1]);
            }
        }

    }

    @Override
    public void mutate(BiomeMutator mutator) {

    }

    @Override
    public void fill(BiomeSource source) {
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < width; z++) {
                biomes[x][z] = source.getBiome(original.getX() + x, original.getY() + z);
            }
        }
    }

    @Override
    public Biome getBiome(int x, int z) {
        return biomes[x][z];
    }
}
