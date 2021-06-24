package com.dfsek.terra.world.population.items.ores;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.vector.Vector3Impl;

import java.util.Map;
import java.util.Random;

public class DeformedSphereOre extends Ore {
    private final double deform;
    private final double deformFrequency;
    private final Range size;

    public DeformedSphereOre(BlockData material, MaterialSet replaceable, boolean applyGravity, double deform, double deformFrequency, Range size, TerraPlugin main, Map<BlockType, BlockData> materials) {
        super(material, replaceable, applyGravity, main, materials);
        this.deform = deform;
        this.deformFrequency = deformFrequency;
        this.size = size;
    }


    @Override
    public void generate(Vector3Impl origin, Chunk c, Random r) {
        OpenSimplex2Sampler ore = new OpenSimplex2Sampler(r.nextInt());
        ore.setFrequency(deformFrequency);
        int rad = size.get(r);
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector3 oreLoc = origin.clone().add(new Vector3Impl(x, y, z));
                    if(oreLoc.getBlockX() > 15 || oreLoc.getBlockZ() > 15 || oreLoc.getBlockY() > c.getWorld().getMaxHeight() || oreLoc.getBlockX() < 0 || oreLoc.getBlockZ() < 0 || oreLoc.getBlockY() < c.getWorld().getMinHeight())
                        continue;
                    if(oreLoc.distance(origin) < (rad + 0.5) * ((ore.getNoise(x, y, z) + 1) * deform)) {
                        Block b = c.getBlock(oreLoc.getBlockX(), oreLoc.getBlockY(), oreLoc.getBlockZ());
                        BlockType type = b.getType();
                        if(getReplaceable().contains(type) && b.getLocation().getY() >= c.getWorld().getMinHeight())
                            b.setBlockData(getMaterial(type), isApplyGravity());
                    }
                }
            }
        }
    }
}
