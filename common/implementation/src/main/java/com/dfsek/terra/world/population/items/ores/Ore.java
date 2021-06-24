package com.dfsek.terra.world.population.items.ores;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.vector.Vector3Impl;

import java.util.Map;
import java.util.Random;

public abstract class Ore {

    private final BlockData material;
    private final MaterialSet replaceable;
    private final boolean applyGravity;
    protected TerraPlugin main;
    private final Map<BlockType, BlockData> materials;

    public Ore(BlockData material, MaterialSet replaceable, boolean applyGravity, TerraPlugin main, Map<BlockType, BlockData> materials) {
        this.material = material;
        this.replaceable = replaceable;
        this.applyGravity = applyGravity;
        this.main = main;
        this.materials = materials;
    }

    public abstract void generate(Vector3Impl origin, Chunk c, Random r);

    public BlockData getMaterial(BlockType replace) {
        return materials.getOrDefault(replace, material);
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
