package com.dfsek.terra.generation;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.Vector;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.Map;
import java.util.Random;

public class SlabGenerator extends GenerationPopulator {
    private static final BlockData AIR = Material.AIR.createBlockData();
    private static final Palette<BlockData> AIRPALETTE = new RandomPalette<BlockData>(new Random(2403)).add(AIR, 1);
    @Override
    public ChunkGenerator.ChunkData populate(World world, ChunkGenerator.ChunkData chunk, Random random, int chunkX, int chunkZ, ChunkInterpolator interp) {
        TerraBiomeGrid g = TerraBiomeGrid.fromWorld(world);
        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                int xi = (chunkX << 4) + x;
                int zi = (chunkZ << 4) + z;
                BiomeConfig config = BiomeConfig.fromBiome((UserDefinedBiome) g.getBiome(xi, zi));
                if(config.getSlabs() == null) continue;
                double thresh = config.getSlabThreshold();
                for(int y = 0; y < world.getMaxHeight(); y++) {
                    if(chunk.getType(x, y, z).isSolid()) continue;
                    prepareBlockPart(interp, chunk, new Vector(x, y, z), config.getSlabs(), config.getStairs(), thresh);
                }
            }
        }
        return chunk;
    }
    private static void prepareBlockPart(ChunkInterpolator interp, ChunkGenerator.ChunkData chunk, Vector block, Map<Material, Palette<BlockData>> slabs, Map<Material, Palette<BlockData>> stairs, double thresh) {
        BlockData down = chunk.getBlockData(block.getBlockX(), block.getBlockY()-1, block.getBlockZ());
        double _11 = interp.getNoise(block.getBlockX(), block.getBlockY() - 0.5, block.getBlockZ());
        if(_11 > thresh) {
            double _00 = interp.getNoise(block.getBlockX() - 0.5, block.getBlockY(), block.getBlockZ() - 0.5);
            double _01 = interp.getNoise(block.getBlockX() - 0.5, block.getBlockY(), block.getBlockZ());
            double _02 = interp.getNoise(block.getBlockX() - 0.5, block.getBlockY(), block.getBlockZ() + 0.5);
            double _10 = interp.getNoise(block.getBlockX(), block.getBlockY(), block.getBlockZ() - 0.5);
            double _12 = interp.getNoise(block.getBlockX(), block.getBlockY(), block.getBlockZ() + 0.5);
            double _20 = interp.getNoise(block.getBlockX() + 0.5, block.getBlockY(), block.getBlockZ() - 0.5);
            double _21 = interp.getNoise(block.getBlockX() + 0.5, block.getBlockY(), block.getBlockZ());
            double _22 = interp.getNoise(block.getBlockX() + 0.5, block.getBlockY(), block.getBlockZ() + 0.5);

            if(stairs != null) {
                Palette<BlockData> stairPalette = stairs.get(down.getMaterial());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getBlockX(), block.getBlockZ());
                    Stairs finalStair = getStair(new double[] {_00, _01, _02, _10, _12, _20, _21, _22}, (Stairs) stair, thresh);
                    if(finalStair != null) {
                        chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), finalStair);
                        return;
                    }
                }
            }
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slabs.getOrDefault(down.getMaterial(), AIRPALETTE).get(0, block.getBlockX(), block.getBlockZ()));
        }
    }

    private static Stairs getStair(double[] vals, Stairs stair, double thresh) {
        if(vals.length != 8) throw new IllegalArgumentException();
        Stairs stairNew = (Stairs) stair.clone();
        if(vals[1] > thresh) {
            stairNew.setFacing(BlockFace.WEST);
        } else if(vals[3] > thresh) {
            stairNew.setFacing(BlockFace.NORTH);
        } else if(vals[4] > thresh) {
            stairNew.setFacing(BlockFace.SOUTH);
        } else if(vals[6] > thresh) {
            stairNew.setFacing(BlockFace.EAST);
        } else return null;
        return stairNew;
    }
}
