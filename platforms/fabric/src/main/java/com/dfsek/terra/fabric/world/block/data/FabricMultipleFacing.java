package com.dfsek.terra.fabric.world.block.data;

import com.dfsek.terra.api.platform.world.block.BlockFace;
import com.dfsek.terra.api.platform.world.block.data.MultipleFacing;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;

import java.util.HashSet;
import java.util.Set;

public class FabricMultipleFacing extends FabricBlockData implements MultipleFacing {
    public FabricMultipleFacing(BlockState delegate) {
        super(delegate);
    }

    @Override
    public Set<BlockFace> getFaces() {
        Set<BlockFace> set = new HashSet<>();
        if(delegate.get(Properties.NORTH)) set.add(BlockFace.NORTH);
        if(delegate.get(Properties.SOUTH)) set.add(BlockFace.SOUTH);
        if(delegate.get(Properties.EAST)) set.add(BlockFace.EAST);
        if(delegate.get(Properties.WEST)) set.add(BlockFace.WEST);
        if(delegate.contains(Properties.UP) && delegate.get(Properties.UP)) set.add(BlockFace.UP);
        if(delegate.contains(Properties.DOWN) && delegate.get(Properties.DOWN)) set.add(BlockFace.DOWN);
        return set;
    }

    @Override
    public void setFace(BlockFace face, boolean facing) {
        switch(face) {
            case NORTH:
                delegate = delegate.with(Properties.NORTH, facing);
                break;
            case SOUTH:
                delegate = delegate.with(Properties.SOUTH, facing);
                break;
            case EAST:
                delegate = delegate.with(Properties.EAST, facing);
                break;
            case WEST:
                delegate = delegate.with(Properties.WEST, facing);
                break;
            case UP:
                delegate = delegate.with(Properties.UP, facing);
                break;
            case DOWN:
                delegate = delegate.with(Properties.DOWN, facing);
                break;
        }
    }

    @Override
    public Set<BlockFace> getAllowedFaces() {
        Set<BlockFace> set = new HashSet<>();
        if(delegate.contains(Properties.NORTH)) set.add(BlockFace.NORTH);
        if(delegate.contains(Properties.SOUTH)) set.add(BlockFace.SOUTH);
        if(delegate.contains(Properties.EAST)) set.add(BlockFace.EAST);
        if(delegate.contains(Properties.WEST)) set.add(BlockFace.WEST);
        if(delegate.contains(Properties.UP)) set.add(BlockFace.UP);
        if(delegate.contains(Properties.DOWN)) set.add(BlockFace.DOWN);
        return set;
    }

    @Override
    public boolean hasFace(BlockFace f) {
        return getFaces().contains(f);
    }
}
