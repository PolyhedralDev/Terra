package com.dfsek.terra.fabric.block;

import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.fabric.util.FabricAdapter;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class FabricBlock implements Block {
    private final Handle delegate;

    public FabricBlock(BlockPos position, WorldAccess worldAccess) {
        this.delegate = new Handle(position, worldAccess);
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        delegate.worldAccess.setBlockState(delegate.position, ((FabricBlockData) data).getHandle(), physics ? 3 : 1042);
        if(physics && ((FabricBlockData) data).getHandle().getBlock() instanceof FluidBlock) {
            delegate.worldAccess.getFluidTickScheduler().schedule(delegate.position, ((FluidBlock) ((FabricBlockData) data).getHandle().getBlock()).getFluidState(((FabricBlockData) data).getHandle()).getFluid(), 0);
        }
    }

    @Override
    public BlockData getBlockData() {
        return new FabricBlockData(delegate.worldAccess.getBlockState(delegate.position));
    }

    @Override
    public BlockState getState() {
        return FabricAdapter.adapt(this);
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        BlockPos newPos = delegate.position.add(face.getModX() * len, face.getModY() * len, face.getModZ() * len);
        return new FabricBlock(newPos, delegate.worldAccess);
    }

    @Override
    public boolean isEmpty() {
        return getBlockData().isAir();
    }

    @Override
    public Location getLocation() {
        return FabricAdapter.adapt(delegate.position).toLocation((World) delegate.worldAccess);
    }

    @Override
    public BlockType getType() {
        return getBlockData().getBlockType();
    }

    @Override
    public int getX() {
        return delegate.position.getX();
    }

    @Override
    public int getZ() {
        return delegate.position.getZ();
    }

    @Override
    public int getY() {
        return delegate.position.getY();
    }

    @Override
    public boolean isPassable() {
        return isEmpty();
    }

    @Override
    public Handle getHandle() {
        return delegate;
    }

    public static final class Handle {
        private final BlockPos position;
        private final WorldAccess worldAccess;

        public Handle(BlockPos position, WorldAccess worldAccess) {
            this.position = position;
            this.worldAccess = worldAccess;
        }
    }
}
