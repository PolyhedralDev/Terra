/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore.v2.ores;

import net.jafama.FastMath;

import java.util.BitSet;
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
    
    protected static boolean shouldNotDiscard(Random random, double chance) {
        if(chance <= 0.0F) {
            return true;
        } else if(chance >= 1.0F) {
            return false;
        } else {
            return random.nextFloat() >= chance;
        }
    }
    
    public static double lerp(double t, double v0, double v1) {
        return v0 + t * (v1 - v0);
    }
    
    @Override
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation) {
        float f = random.nextFloat() * (float) Math.PI;
        double g = size / 8.0F;
        int i = (int) FastMath.ceil((size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double startX = (double) location.getX() + FastMath.sin(f) * g;
        double endX = (double) location.getX() - FastMath.sin(f) * g;
        double startZ = (double) location.getZ() + FastMath.cos(f) * g;
        double endZ = (double) location.getZ() - FastMath.cos(f) * g;
        double startY = location.getY() + random.nextInt(3) - 2;
        double endY = location.getY() + random.nextInt(3) - 2;
        int x = (int) (location.getX() - FastMath.ceil(g) - i);
        int y = location.getY() - 2 - i;
        int z = (int) (location.getZ() - FastMath.ceil(g) - i);
        int horizontalSize = (int) (2 * (FastMath.ceil(g) + i));
        int verticalSize = 2 * (2 + i);
        
        int i1 = 0;
        BitSet bitSet = new BitSet(horizontalSize * verticalSize * horizontalSize);
        
        int j1 = (int) size;
        double[] ds = new double[j1 * 4];
        
        for(int k1 = 0; k1 < j1; ++k1) {
            float f1 = (float) k1 / (float) j1;
            double d1 = lerp(f1, startX, endX);
            double e1 = lerp(f1, startY, endY);
            double g1 = lerp(f1, startZ, endZ);
            double h1 = random.nextDouble() * (double) j1 / 16.0;
            double l1 = ((FastMath.sin((float) Math.PI * f1) + 1.0F) * h1 + 1.0) / 2.0;
            ds[k1 * 4] = d1;
            ds[k1 * 4 + 1] = e1;
            ds[k1 * 4 + 2] = g1;
            ds[k1 * 4 + 3] = l1;
        }
        
        for(int k1 = 0; k1 < j1 - 1; ++k1) {
            if(!(ds[k1 * 4 + 3] <= 0.0)) {
                for(int m1 = k1 + 1; m1 < j1; ++m1) {
                    if(!(ds[m1 * 4 + 3] <= 0.0)) {
                        double d1 = ds[k1 * 4] - ds[m1 * 4];
                        double e1 = ds[k1 * 4 + 1] - ds[m1 * 4 + 1];
                        double g1 = ds[k1 * 4 + 2] - ds[m1 * 4 + 2];
                        double h1 = ds[k1 * 4 + 3] - ds[m1 * 4 + 3];
                        if(h1 * h1 > d1 * d1 + e1 * e1 + g1 * g1) {
                            if(h1 > 0.0) {
                                ds[m1 * 4 + 3] = -1.0;
                            } else {
                                ds[k1 * 4 + 3] = -1.0;
                            }
                        }
                    }
                }
            }
        }
        
        for(int m1 = 0; m1 < j1; ++m1) {
            double d1 = ds[m1 * 4 + 3];
            if(!(d1 < 0.0)) {
                double e1 = ds[m1 * 4];
                double g1 = ds[m1 * 4 + 1];
                double h1 = ds[m1 * 4 + 2];
                int n1 = (int) FastMath.max(FastMath.floor(e1 - d1), x);
                int o1 = (int) FastMath.max(FastMath.floor(g1 - d1), y);
                int p1 = (int) FastMath.max(FastMath.floor(h1 - d1), z);
                int q1 = (int) FastMath.max(FastMath.floor(e1 + d1), n1);
                int r1 = (int) FastMath.max(FastMath.floor(g1 + d1), o1);
                int s1 = (int) FastMath.max(FastMath.floor(h1 + d1), p1);
                
                for(int t1 = n1; t1 <= q1; ++t1) {
                    double u1 = ((double) t1 + 0.5 - e1) / d1;
                    if(u1 * u1 < 1.0) {
                        for(int v1 = o1; v1 <= r1; ++v1) {
                            double w1 = ((double) v1 + 0.5 - g1) / d1;
                            if(u1 * u1 + w1 * w1 < 1.0) {
                                for(int aa = p1; aa <= s1; ++aa) {
                                    double ab = ((double) aa + 0.5 - h1) / d1;
                                    if(u1 * u1 + w1 * w1 + ab * ab < 1.0 && !(v1 < world.getMinHeight() || v1 >= world.getMaxHeight())) {
                                        int ac = t1 - x + (v1 - y) * horizontalSize + (aa - z) * horizontalSize * verticalSize;
                                        if(!bitSet.get(ac)) {
                                            bitSet.set(ac);
                                            
                                            BlockType block = world.getBlockState(x, y, z).getBlockType();
                                            
                                            if(shouldPlace(block, random, world, t1, v1, aa)) {
                                                world.setBlockState(t1, v1, aa, getMaterial(block), isApplyGravity());
                                                ++i1;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        
        return i1 > 0;
    }
    
    public boolean shouldPlace(BlockType type, Random random, WritableWorld world, int x, int y, int z) {
        if(!getReplaceable().contains(type)) {
            return false;
        } else if(shouldNotDiscard(random, exposed)) {
            return true;
        } else {
            return !(world.getBlockState(x, y, z - 1).isAir() ||
                     world.getBlockState(x, y, z + 1).isAir() ||
                     world.getBlockState(x, y - 1, z).isAir() ||
                     world.getBlockState(x, y + 1, z).isAir() ||
                     world.getBlockState(x - 1, y, z).isAir() ||
                     world.getBlockState(x + 1, y, z).isAir());
        }
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
