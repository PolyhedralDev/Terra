package com.dfsek.terra.generation.items.ores;

import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.WorldHandle;
import com.dfsek.terra.api.platform.world.block.Block;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.vector.Vector3;
import com.dfsek.terra.util.MaterialSet;
import net.jafama.FastMath;

import java.util.Random;


public class VanillaOre extends Ore {
    private final Range sizeRange;

    public VanillaOre(BlockData material, MaterialSet replaceable, boolean applyGravity, Range size, TerraPlugin main) {
        super(material, replaceable, applyGravity, main);
        this.sizeRange = size;
    }

    @Override
    public void generate(Vector3 location, Chunk chunk, Random random) {
        WorldHandle handle = main.getWorldHandle();
        double size = sizeRange.get(random);

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
                                if(x > 15 || z > 15 || y > 255 || x < 0 || z < 0 || y < 0) continue;
                                Block block = chunk.getBlock(x, y, z);
                                if((d13 * d13 + d14 * d14 + d15 * d15 < 1.0D) && getReplaceable().contains(block.getType())) {
                                    handle.setBlockData(block, getMaterial(), isApplyGravity());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
