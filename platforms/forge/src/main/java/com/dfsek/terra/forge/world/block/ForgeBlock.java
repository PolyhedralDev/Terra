package com.dfsek.terra.forge.world.block;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.forge.world.ForgeAdapter;
import com.dfsek.terra.forge.world.block.state.ForgeBlockState;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldAccess;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class ForgeBlock implements Block {
    private final Handle delegate;

    public ForgeBlock(BlockPos position, IWorld worldAccess) {
        this.delegate = new Handle(position, worldAccess);
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        delegate.worldAccess.setBlock(delegate.position, ((ForgeBlockData) data).getHandle(), physics ? 3 : 1042);
        if(physics && ((ForgeBlockData) data).getHandle().getBlock() instanceof FlowingFluidBlock) {
            delegate.worldAccess.getLiquidTicks().scheduleTick(delegate.position, ((FlowingFluidBlock) ((ForgeBlockData) data).getHandle().getBlock()).getFluidState(((ForgeBlockData) data).getHandle()).getFluidState().getType(), 0);
        }
    }

    @Override
    public BlockData getBlockData() {
        return new ForgeBlockData(delegate.worldAccess.getBlockState(delegate.position));
    }

    @Override
    public BlockState getState() {
        return ForgeBlockState.newInstance(this);
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        BlockPos newPos = delegate.position.offset(face.getModX() * len, face.getModY() * len, face.getModZ() * len);
        return new ForgeBlock(newPos, delegate.worldAccess);
    }

    @Override
    public boolean isEmpty() {
        return getBlockData().isAir();
    }

    @Override
    public Location getLocation() {
        return ForgeAdapter.adapt(delegate.position).toLocation(new ForgeWorldAccess(delegate.worldAccess));
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
        private final IWorld worldAccess;

        public Handle(BlockPos position, IWorld worldAccess) {
            this.position = position;
            this.worldAccess = worldAccess;
        }
    }
}
