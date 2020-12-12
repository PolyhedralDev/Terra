package com.dfsek.terra.generation.items.ores;

import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;

public class DeformedSphereOre extends Ore {
    private final double deform;
    private final double deformFrequency;
    private final Range size;

    public DeformedSphereOre(BlockData material, MaterialSet replaceable, boolean applyGravity, double deform, double deformFrequency, Range size, TerraPlugin main) {
        super(material, replaceable, applyGravity, main);
        this.deform = deform;
        this.deformFrequency = deformFrequency;
        this.size = size;
    }


    @Override
    public void generate(Vector3 origin, Chunk c, Random r) {
        WorldHandle handle = main.getWorldHandle();
        FastNoiseLite ore = new FastNoiseLite(r.nextInt());
        ore.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        ore.setFrequency(deformFrequency);
        int rad = size.get(r);
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector3 oreLoc = origin.clone().add(new Vector3(x, y, z));
                    if(oreLoc.getBlockX() > 15 || oreLoc.getBlockZ() > 15 || oreLoc.getBlockY() > 255 || oreLoc.getBlockX() < 0 || oreLoc.getBlockZ() < 0 || oreLoc.getBlockY() < 0)
                        continue;
                    if(oreLoc.distance(origin) < (rad + 0.5) * ((ore.getNoise(x, y, z) + 1) * deform)) {
                        Block b = c.getBlock(oreLoc.getBlockX(), oreLoc.getBlockY(), oreLoc.getBlockZ());
                        if(getReplaceable().contains(b.getType()) && b.getLocation().getY() >= 0)
                            handle.setBlockData(b, getMaterial(), isApplyGravity());
                    }
                }
            }
        }
    }
}
