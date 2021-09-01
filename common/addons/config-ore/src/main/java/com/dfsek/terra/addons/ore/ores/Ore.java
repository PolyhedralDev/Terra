package com.dfsek.terra.addons.ore.ores;

import java.util.Map;
import java.util.Random;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;


public abstract class Ore {
    
    private final BlockState material;
    private final MaterialSet replaceable;
    private final boolean applyGravity;
    private final Map<BlockType, BlockState> materials;
    protected TerraPlugin main;
    
    public Ore(BlockState material, MaterialSet replaceable, boolean applyGravity, TerraPlugin main, Map<BlockType, BlockState> materials) {
        this.material = material;
        this.replaceable = replaceable;
        this.applyGravity = applyGravity;
        this.main = main;
        this.materials = materials;
    }
    
    public abstract void generate(Vector3 origin, Chunk c, Random r);
    
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
