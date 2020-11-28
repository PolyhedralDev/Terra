package com.dfsek.terra.generation.items.ores;

import net.jafama.FastMath;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.Random;
import java.util.Set;


public class VanillaOre extends Ore {
    private final double size;

    public VanillaOre(BlockData material, Set<Material> replaceable, boolean applyGravity, double size) {
        super(material, replaceable, applyGravity);
        this.size = size;
    }


    public void generate(Location location, Chunk chunk, Random random) {
        int chunkBoundx1 = chunk.getX() << 4;
        int chunkBoundx2 = chunk.getX() << 4 + 15;
        int chunkBoundz1 = chunk.getZ() << 4;
        int chunkBoundz2 = chunk.getZ() << 4 + 15;

        int centerX = location.getBlockX();
        int centerZ = location.getBlockZ();
        int centerY = location.getBlockY();


        float f = random.nextFloat() * (float) Math.PI;

        double d1 = centerX + 8 + FastMath.sin(f) * size / 8.0F;
        double d2 = centerX + 8 - FastMath.sin(f) * size / 8.0F;
        double d3 = centerZ + 8 + FastMath.cos(f) * size / 8.0F;
        double d4 = centerZ + 8 - FastMath.cos(f) * size / 8.0F;

        double d5 = centerY + random.nextInt(3) - 2D;
        double d6 = centerY + random.nextInt(3) - 2D;

        for(int i = 0; i < size; i++) {
            float iFactor = (float) i / (float) size;

            double d10 = random.nextDouble() * size / 16.0D;
            double d11 = (FastMath.sin(Math.PI * iFactor) + 1.0) * d10 + 1.0;
            double d12 = (FastMath.sin(Math.PI * iFactor) + 1.0) * d10 + 1.0;

            int xStart = FastMath.roundToInt(FastMath.floor(d1 + (d2 - d1) * iFactor - d11 / 2.0D));
            int yStart = FastMath.roundToInt(FastMath.floor(d5 + (d6 - d5) * iFactor - d12 / 2.0D));
            int zStart = FastMath.roundToInt(FastMath.floor(d3 + (d4 - d3) * iFactor - d11 / 2.0D));

            int xEnd = FastMath.roundToInt(FastMath.floor(d1 + (d2 - d1) * iFactor + d11 / 2.0D));
            int yEnd = FastMath.roundToInt(FastMath.floor(d5 + (d6 - d5) * iFactor + d12 / 2.0D));
            int zEnd = FastMath.roundToInt(FastMath.floor(d3 + (d4 - d3) * iFactor + d11 / 2.0D));

            for(int x = xStart; x <= xEnd; x++) {
                double d13 = (x + 0.5D - (d1 + (d2 - d1) * iFactor)) / (d11 / 2.0D);

                if(d13 * d13 < 1.0D) {
                    for(int y = yStart; y <= yEnd; y++) {
                        double d14 = (y + 0.5D - (d5 + (d6 - d5) * iFactor)) / (d12 / 2.0D);
                        if(d13 * d13 + d14 * d14 < 1.0D) {
                            for(int z = zStart; z <= zEnd; z++) {
                                double d15 = (z + 0.5D - (d3 + (d4 - d3) * iFactor)) / (d11 / 2.0D);
                                Block block = chunk.getBlock(x, y, z);
                                if((block.getX() <= chunkBoundx1 || block.getX() >= chunkBoundx2) &&
                                        (block.getZ() <= chunkBoundz1 || block.getZ() >= chunkBoundz2) &&
                                        block.getY() <= 255)
                                    continue;
                                if((d13 * d13 + d14 * d14 + d15 * d15 < 1.0D) && getReplaceable().contains(block.getType())) {
                                    block.setBlockData(getMaterial(), isApplyGravity());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
