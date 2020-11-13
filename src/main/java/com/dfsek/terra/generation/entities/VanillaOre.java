package com.dfsek.terra.generation.entities;

import net.royawesome.jlibnoise.MathHelper;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.Set;

public class VanillaOre extends Ore {
    private final int size;

    public VanillaOre(BlockData oreData, int size, double deform, double deformFrequency, String id,
                      boolean update,
                      int chunkEdgeOffset, Set<Material> replaceable) {
        super(oreData, deform, deformFrequency, id, update, chunkEdgeOffset, replaceable);
        this.size = size;
    }

    /**
     * TODO: what the fuck does half of this do?
     *
     * @param location
     * @param random
     * @param plugin
     */
    @Override
    public void generate(Location location, Random random, JavaPlugin plugin) {
        Chunk chunk = location.getChunk();

        int centerX = location.getBlockX();
        int centerZ = location.getBlockZ();
        int centerY = location.getBlockY();


        float f = random.nextFloat() * (float) Math.PI;

        double d1 = centerX + 8 + MathHelper.sin(f) * size / 8.0F;
        double d2 = centerX + 8 - MathHelper.sin(f) * size / 8.0F;
        double d3 = centerZ + 8 + MathHelper.cos(f) * size / 8.0F;
        double d4 = centerZ + 8 - MathHelper.cos(f) * size / 8.0F;

        double d5 = centerY + random.nextInt(3) - 2;
        double d6 = centerY + random.nextInt(3) - 2;

        for(int i = 0; i < size; i++) {
            float iFactor = (float) i / (float) size;

            double d10 = random.nextDouble() * size / 16.0D;
            double d11 = (MathHelper.sin((float) Math.PI * iFactor) + 1.0) * d10 + 1.0;
            double d12 = (MathHelper.sin((float) Math.PI * iFactor) + 1.0) * d10 + 1.0;

            int xStart = MathHelper.floor(d1 + (d2 - d1) * iFactor - d11 / 2.0D);
            int yStart = MathHelper.floor(d5 + (d6 - d5) * iFactor - d12 / 2.0D);
            int zStart = MathHelper.floor(d3 + (d4 - d3) * iFactor - d11 / 2.0D);

            int xEnd = MathHelper.floor(d1 + (d2 - d1) * iFactor + d11 / 2.0D);
            int yEnd = MathHelper.floor(d5 + (d6 - d5) * iFactor + d12 / 2.0D);
            int zEnd = MathHelper.floor(d3 + (d4 - d3) * iFactor + d11 / 2.0D);

            for(int x = xStart; x <= xEnd; x++) {
                double d13 = (x + 0.5D - (d1 + (d2 - d1) * iFactor)) / (d11 / 2.0D);

                if(d13 * d13 < 1.0D) {
                    for(int y = yStart; y <= yEnd; y++) {
                        double d14 = (y + 0.5D - (d5 + (d6 - d5) * iFactor)) / (d12 / 2.0D);
                        if(d13 * d13 + d14 * d14 < 1.0D) {
                            for(int z = zStart; z <= zEnd; z++) {
                                double d15 = (z + 0.5D - (d3 + (d4 - d3) * iFactor)) / (d11 / 2.0D);
                                Block block = chunk.getBlock(x, y, z);
                                if((d13 * d13 + d14 * d14 + d15 * d15 < 1.0D) && replaceable.contains(block.getType())) {
                                    block.setBlockData(oreData, update);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
