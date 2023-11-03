package com.dfsek.terra.addons.ore.ores;

import java.util.Map;
import java.util.Random;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;

import static com.dfsek.terra.addons.ore.utils.VanillaOreUtils.shouldPlace;


public class VanillaScatteredOre extends VanillaOre {
    protected final int spread;
    public VanillaScatteredOre(BlockState material, double size, MaterialSet replaceable, boolean applyGravity, double exposed,
                               Map<BlockType, BlockState> materials, int spread) {
        super(material, size, replaceable, applyGravity, exposed, materials);
        
        this.spread = spread;
    }
    
    @Override
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation) {
        int i = random.nextInt((int) (size + 1));
        Vector3Int.Mutable mutable = Vector3Int.zero().mutable();
        
        for(int j = 0; j < i; ++j) {
            this.setPos(mutable, random, location, Math.min(j, spread));
            BlockType block = world.getBlockState(mutable).getBlockType();
                if (shouldPlace(getReplaceable(), block, exposed, random, world, mutable.getX(), mutable.getY(), mutable.getZ())) {
                    world.setBlockState(mutable, getMaterial(block), isApplyGravity());
                }
        }
        
        return true;
    }
    
    private void setPos(Vector3Int.Mutable mutable, Random random, Vector3Int location, int spread) {
        int x = this.getSpread(random, spread);
        int y = this.getSpread(random, spread);
        int z = this.getSpread(random, spread);
        mutable.setX(location.getX() + x);
        mutable.setY(location.getY() + y);
        mutable.setZ(location.getZ() + z);
    }
    
    private int getSpread(Random random, int spread) {
        return Math.round((random.nextFloat() - random.nextFloat()) * (float)spread);
    }
}
