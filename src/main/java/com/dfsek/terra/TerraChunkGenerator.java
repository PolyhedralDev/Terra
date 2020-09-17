package com.dfsek.terra;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FaunaPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.TreePopulator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.InterpolationType;
import org.polydev.gaea.math.MathUtil;
import org.polydev.gaea.population.PopulationManager;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TerraChunkGenerator extends GaeaChunkGenerator {
    private final PopulationManager popMan = new PopulationManager();
    public TerraChunkGenerator() {
        super(InterpolationType.TRILINEAR);
        popMan.attach(new TreePopulator());
        popMan.attach(new FaunaPopulator());
        popMan.attach(new OrePopulator());
    }

    @Override
    public ChunkData generateBase(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, FastNoise fastNoise) {
        ChunkData chunk = createChunkData(world);
        int sea = WorldConfig.fromWorld(world).seaLevel;
        for(byte x = 0; x < 16; x++) {
            for(int y = 0; y < 256; y++) {
                for(byte z = 0; z < 16; z++) {
                    if(super.getInterpolatedNoise(x, y, z) > 0) chunk.setBlock(x, y, z, Material.STONE);
                    else if(y < sea) chunk.setBlock(x, y, z, Material.WATER);
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
        return Collections.singletonList(new CavePopulator());
    }

    @Override
    public org.polydev.gaea.biome.BiomeGrid getBiomeGrid(World world) {
        return TerraBiomeGrid.fromWorld(world);
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.singletonList(popMan);
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }
}
