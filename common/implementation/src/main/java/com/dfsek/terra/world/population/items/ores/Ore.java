package com.dfsek.terra.world.population.items.ores;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.vector.Vector3Impl;

import java.util.Map;
import java.util.Random;

public abstract class Ore {

    private final BlockState material;
    private final MaterialSet replaceable;
    private final boolean applyGravity;
    protected TerraPlugin main;
    private final Map<BlockType, BlockState> materials;

    public Ore(BlockState material, MaterialSet replaceable, boolean applyGravity, TerraPlugin main, Map<BlockType, BlockState> materials) {
        this.material = material;
        this.replaceable = replaceable;
        this.applyGravity = applyGravity;
        this.main = main;
        this.materials = materials;
    }

    public abstract void generate(Vector3Impl origin, Chunk c, Random r);

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
