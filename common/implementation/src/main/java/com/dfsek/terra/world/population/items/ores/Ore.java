package com.dfsek.terra.world.population.items.ores;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.util.collections.MaterialSet;

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

    public abstract void generate(Vector3 origin, Chunk c, Random r);

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
