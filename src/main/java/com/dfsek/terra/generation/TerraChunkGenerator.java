package com.dfsek.terra.generation;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.TreePopulator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.population.PopulationManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TerraChunkGenerator extends GaeaChunkGenerator {
    private static final BlockData STONE = Material.STONE.createBlockData();
    private static final BlockData WATER = Material.WATER.createBlockData();
    private final PopulationManager popMan = new PopulationManager();
    public TerraChunkGenerator() {
        super(ChunkInterpolator.InterpolationType.TRILINEAR);
        popMan.attach(new TreePopulator());
        popMan.attach(new FloraPopulator());
        popMan.attach(new OrePopulator());
    }

    @Override
    public ChunkData generateBase(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, FastNoise fastNoise) {
        ChunkData chunk = createChunkData(world);
        int sea = WorldConfig.fromWorld(world).seaLevel;
        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                for(int y = 0; y < 256; y++) {
                    if(super.getInterpolatedNoise(x, y, z) > 0) chunk.setBlock(x, y, z, STONE);
                    else if(y < sea) chunk.setBlock(x, y, z, WATER);
                }
            }
        }
        return chunk;
    }

    @Override
    public int getNoiseOctaves(World world) {
        return 4;
    }
    @Override
    public float getNoiseFrequency(World world) {
        return 1f/96;
    }

    @Override
    public List<GenerationPopulator> getGenerationPopulators(World world) {
        return Collections.singletonList(new SlabGenerator());
    }


    @Override
    public org.polydev.gaea.biome.BiomeGrid getBiomeGrid(World world) {
        return TerraBiomeGrid.fromWorld(world);
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Arrays.asList(new CavePopulator(), popMan);
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }
}
