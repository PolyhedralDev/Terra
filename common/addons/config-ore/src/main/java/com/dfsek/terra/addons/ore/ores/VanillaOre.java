/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore.ores;

import net.jafama.FastMath;

import java.util.Map;
import java.util.Random;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;


public class VanillaOre implements Structure {
    
    private final BlockState material;
    
    private final double size;
    private final MaterialSet replaceable;
    private final boolean applyGravity;
    private final double exposed;
    private final Map<BlockType, BlockState> materials;
    
    public VanillaOre(BlockState material, double size, MaterialSet replaceable, boolean applyGravity,
                      double exposed, Map<BlockType, BlockState> materials) {
        this.material = material;
        this.size = size;
        this.replaceable = replaceable;
        this.applyGravity = applyGravity;
        this.exposed = exposed;
        this.materials = materials;
    }
    
    @Override
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation) {
        int centerX = location.getX();
        int centerZ = location.getZ();
        int centerY = location.getY();
        
        
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
                                if(y >= world.getMaxHeight() || y < world.getMinHeight()) continue;
                                BlockType block = world.getBlockState(x, y, z).getBlockType();
                                if((d13 * d13 + d14 * d14 + d15 * d15 < 1.0D) && getReplaceable().contains(block)) {
                                    if(exposed > random.nextDouble() || !(world.getBlockState(x, y, z - 1).isAir() ||
                                                                          world.getBlockState(x, y, z + 1).isAir() ||
                                                                          world.getBlockState(x, y - 1, z).isAir() ||
                                                                          world.getBlockState(x, y + 1, z).isAir() ||
                                                                          world.getBlockState(x - 1, y, z).isAir() ||
                                                                          world.getBlockState(x + 1, y, z).isAir())) {
                                        world.setBlockState(x, y, z, getMaterial(block), isApplyGravity());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public BlockState getMaterial(BlockType replace) {
        return materials.getOrDefault(replace, material);
    }
    
    public MaterialSet getReplaceable() {
        return replaceable;
    }
    
    public boolean isApplyGravity() {
        return applyGravity;
    }
}
