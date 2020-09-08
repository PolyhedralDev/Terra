package com.dfsek.terra;

import com.dfsek.terra.biome.TerraBiomeGrid;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.InterpolationType;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TerraChunkGenerator extends GaeaChunkGenerator {
    public TerraChunkGenerator() {
        super(InterpolationType.TRILINEAR);
    }

    public ChunkData generateBase(@NotNull World world, @NotNull Random random, int i, int i1, FastNoise fastNoise) {
        ChunkData chunk = createChunkData(world);
        for(byte x = 0; x < 16; x++) {
            for(int y = 0; y < 256; y++) {
                for(byte z = 0; z < 16; z++) {
                    if(super.getInterpolatedNoise(x, y, z) > 0.5) chunk.setBlock(x, y, z, Material.STONE);
                }
            }
        }
        return chunk;
    }

    public int getNoiseOctaves(World world) {
        return 4;
    }

    public float getNoiseFrequency(World world) {
        return 1f/96;
    }

    public List<GenerationPopulator> getGenerationPopulators(World world) {
        return Collections.emptyList();
    }

    public org.polydev.gaea.biome.BiomeGrid getBiomeGrid(World world) {
        return new TerraBiomeGrid(world);
    }
}
