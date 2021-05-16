package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.MultipleFacing;
import com.dfsek.terra.forge.block.ForgeBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;

import java.util.HashSet;
import java.util.Set;

public class ForgeMultipleFacing extends ForgeBlockData implements MultipleFacing {
    public ForgeMultipleFacing(BlockState delegate) {
        super(delegate);
    }

    @Override
    public Set<BlockFace> getFaces() {
        Set<BlockFace> set = new HashSet<>();
        if(delegate.getValue(BlockStateProperties.NORTH)) set.add(BlockFace.NORTH);
        if(delegate.getValue(BlockStateProperties.SOUTH)) set.add(BlockFace.SOUTH);
        if(delegate.getValue(BlockStateProperties.EAST)) set.add(BlockFace.EAST);
        if(delegate.getValue(BlockStateProperties.WEST)) set.add(BlockFace.WEST);
        if(delegate.hasProperty(BlockStateProperties.UP) && delegate.getValue(BlockStateProperties.UP)) set.add(BlockFace.UP);
        if(delegate.hasProperty(BlockStateProperties.DOWN) && delegate.getValue(BlockStateProperties.DOWN)) set.add(BlockFace.DOWN);
        return set;
    }

    @Override
    public void setFace(BlockFace face, boolean facing) {
        switch(face) {
            case NORTH -> delegate = delegate.setValue(BlockStateProperties.NORTH, facing);
            case SOUTH -> delegate = delegate.setValue(BlockStateProperties.SOUTH, facing);
            case EAST -> delegate = delegate.setValue(BlockStateProperties.EAST, facing);
            case WEST -> delegate = delegate.setValue(BlockStateProperties.WEST, facing);
            case UP -> delegate = delegate.setValue(BlockStateProperties.UP, facing);
            case DOWN -> delegate = delegate.setValue(BlockStateProperties.DOWN, facing);
        }
    }

    @Override
    public Set<BlockFace> getAllowedFaces() {
        Set<BlockFace> set = new HashSet<>();
        if(delegate.hasProperty(BlockStateProperties.NORTH)) set.add(BlockFace.NORTH);
        if(delegate.hasProperty(BlockStateProperties.SOUTH)) set.add(BlockFace.SOUTH);
        if(delegate.hasProperty(BlockStateProperties.EAST)) set.add(BlockFace.EAST);
        if(delegate.hasProperty(BlockStateProperties.WEST)) set.add(BlockFace.WEST);
        if(delegate.hasProperty(BlockStateProperties.UP)) set.add(BlockFace.UP);
        if(delegate.hasProperty(BlockStateProperties.DOWN)) set.add(BlockFace.DOWN);
        return set;
    }

    @Override
    public boolean hasFace(BlockFace f) {
        return getFaces().contains(f);
    }
}
