package com.dfsek.terra.api.world.biome.pipeline;

import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeExpander;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeHolder;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeMutator;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;

public class BiomeHolderImpl implements BiomeHolder {
    private final Vector2 origin;
    private final int width;
    private TerraBiome[][] biomes;
    private final int offset;

    public BiomeHolderImpl(int width, Vector2 origin) {
        width += 4;
        this.width = width;
        biomes = new TerraBiome[width][width];
        this.origin = origin;
        this.offset = 2;
    }

    private BiomeHolderImpl(TerraBiome[][] biomes, Vector2 origin, int width, int offset) {
        this.biomes = biomes;
        this.origin = origin;
        this.width = width;
        this.offset = 2 * offset;
    }

    @Override
    public BiomeHolder expand(BiomeExpander expander) {
        TerraBiome[][] old = biomes;
        int newWidth = width * 2 - 1;

        biomes = new TerraBiome[newWidth][newWidth];

        for(int x = 0; x < width; x++) {
            for(int z = 0; z < width; z++) {
                biomes[x * 2][z * 2] = old[x][z];
                if(z != width - 1)
                    biomes[x * 2][z * 2 + 1] = expander.getBetween(x + origin.getX(), z + 1 + origin.getZ(), old[x][z], old[x][z + 1]);
                if(x != width - 1)
                    biomes[x * 2 + 1][z * 2] = expander.getBetween(x + 1 + origin.getX(), z + origin.getZ(), old[x][z], old[x + 1][z]);
                if(x != width - 1 && z != width - 1)
                    biomes[x * 2 + 1][z * 2 + 1] = expander.getBetween(x + 1 + origin.getX(), z + 1 + origin.getZ(), old[x][z], old[x + 1][z + 1], old[x][z + 1], old[x + 1][z]);
            }
        }
        return new BiomeHolderImpl(biomes, origin.setX(origin.getX() * 2 - 1).setZ(origin.getZ() * 2 - 1), newWidth, offset);
    }

    @Override
    public void mutate(BiomeMutator mutator) {
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < width; z++) {
                BiomeMutator.ViewPoint viewPoint = new BiomeMutator.ViewPoint(this, x, z);
                biomes[x][z] = mutator.mutate(viewPoint, x + origin.getX(), z + origin.getZ());
            }
        }
    }

    @Override
    public void fill(BiomeSource source) {
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < width; z++) {
                biomes[x][z] = source.getBiome(origin.getX() + x, origin.getZ() + z);
            }
        }
    }

    @Override
    public TerraBiome getBiome(int x, int z) {
        x += offset;
        z += offset;
        return getBiomeRaw(x, z);
    }

    @Override
    public TerraBiome getBiomeRaw(int x, int z) {
        if(x >= width || z >= width || x < 0 || z < 0) return null;
        return biomes[x][z];
    }
}
