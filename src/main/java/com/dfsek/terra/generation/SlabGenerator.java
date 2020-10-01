package com.dfsek.terra.generation;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.Vector;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.Map;
import java.util.Random;

public class SlabGenerator extends GenerationPopulator {
    private static final BlockData AIR = Material.AIR.createBlockData();
    private static final BlockData WATER = Material.WATER.createBlockData();
    private static final Palette<BlockData> AIRPALETTE = new RandomPalette<BlockData>(new Random(2403)).add(AIR, 1);
    @Override
    public ChunkGenerator.ChunkData populate(World world, ChunkGenerator.ChunkData chunk, Random random, int chunkX, int chunkZ, ChunkInterpolator interp) {
        TerraBiomeGrid g = TerraBiomeGrid.fromWorld(world);
        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                int xi = (chunkX << 4) + x;
                int zi = (chunkZ << 4) + z;
                BiomeConfig config = BiomeConfig.fromBiome((UserDefinedBiome) g.getBiome(xi, zi, GenerationPhase.PALETTE_APPLY));
                if(config.getSlabs() == null) continue;
                double thresh = config.getSlabThreshold();
                for(int y = 0; y < world.getMaxHeight(); y++) {

                }
            }
        }
        return chunk;
    }



}
