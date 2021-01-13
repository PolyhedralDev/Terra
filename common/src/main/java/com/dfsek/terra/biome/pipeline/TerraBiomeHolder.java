package com.dfsek.terra.biome.pipeline;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.expand.BiomeExpander;
import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.biome.pipeline.source.BiomeSource;

public class TerraBiomeHolder implements BiomeHolder {
    private final Position original;
    private int width;
    private TerraBiome[][] biomes;

    public TerraBiomeHolder(int width, Position original) {
        this.width = width;
        biomes = new TerraBiome[width][width];
        this.original = original;
    }

    @Override
    public void expand(BiomeExpander expander) {
        TerraBiome[][] old = biomes;
        int oldWidth = width;

        width = 2 * width - 1;

        biomes = new TerraBiome[width][width];
        for(int x = 0; x < oldWidth; x++) {
            for(int z = 0; z < oldWidth; z++) {
                biomes[x * 2][z * 2] = old[x][z];
                if(z != oldWidth - 1) biomes[x * 2][z * 2 + 1] = expander.getBetween(new Position(x, z + 1), old[x][z], old[x][z + 1]);
                if(x != oldWidth - 1) biomes[x * 2 + 1][z * 2] = expander.getBetween(new Position(x + 1, z), old[x][z], old[x + 1][z]);
                if(x != oldWidth - 1 && z != oldWidth - 1)
                    biomes[x * 2 + 1][z * 2 + 1] = expander.getBetween(new Position(x + 1, z + 1), old[x][z], old[x + 1][z + 1], old[x][z + 1], old[x + 1][z]);
            }
        }

    }

    @Override
    public void mutate(BiomeMutator mutator) {
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < width; z++) {
                BiomeMutator.ViewPoint viewPoint = new BiomeMutator.ViewPoint(new TerraBiome[][] {
                        {getBiome(x - 1, z + 1), getBiome(x, z + 1), getBiome(x + 1, z + 1)},
                        {getBiome(x - 1, z), getBiome(x, z), getBiome(x + 1, z)},
                        {getBiome(x - 1, z - 1), getBiome(x, z - 1), getBiome(x + 1, z - 1)}
                });
                biomes[x][z] = mutator.mutate(viewPoint, new Position(x, z));
            }
        }
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
    public TerraBiome getBiome(int x, int z) {
        if(x >= width || z >= width || x < 0 || z < 0) return null;
        return biomes[x][z];
    }
}
