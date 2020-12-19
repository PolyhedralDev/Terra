package com.dfsek.terra.generation.items.ores;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.vector.Vector3;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;

public abstract class Ore {

    private final BlockData material;
    private final MaterialSet replaceable;
    private final boolean applyGravity;
    protected TerraPlugin main;

    public Ore(BlockData material, MaterialSet replaceable, boolean applyGravity, TerraPlugin main) {

        this.material = material;
        this.replaceable = replaceable;
        this.applyGravity = applyGravity;
        this.main = main;
    }

    public abstract void generate(Vector3 origin, Chunk c, Random r);

    public BlockData getMaterial() {
        return material;
    }

    public MaterialSet getReplaceable() {
        return replaceable;
    }

    public boolean isApplyGravity() {
        return applyGravity;
    }

    public enum Type {
        VANILLA, SPHERE
    }
}
