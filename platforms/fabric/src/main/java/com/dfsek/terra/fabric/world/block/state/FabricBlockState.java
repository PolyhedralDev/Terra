package com.dfsek.terra.fabric.world.block.state;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.fabric.world.FabricAdapter;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.handles.FabricWorld;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;

public class FabricBlockState implements BlockState {
    protected final BlockEntity blockEntity;

    public FabricBlockState(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public static FabricBlockState newInstance(Block block) {
        net.minecraft.block.Block block1 = ((FabricBlockData) block.getBlockData()).getHandle().getBlock();
        if(block1 instanceof SignBlock) {
            return new FabricSign((SignBlockEntity) ((SignBlock) block1).createBlockEntity(((FabricWorld) block.getLocation().getWorld()).getHandle().getWorld()));
        }
        if(block1 instanceof ChestBlock) {
            return new FabricSign((SignBlockEntity) ((SignBlock) block1).createBlockEntity(((FabricWorld) block.getLocation().getWorld()).getHandle().getWorld()));
        }
        return null;
    }

    @Override
    public BlockEntity getHandle() {
        return blockEntity;
    }

    @Override
    public Block getBlock() {
        return new FabricBlock(blockEntity.getPos(), blockEntity.getWorld());
    }

    @Override
    public int getX() {
        return blockEntity.getPos().getX();
    }

    @Override
    public int getY() {
        return blockEntity.getPos().getY();
    }

    @Override
    public int getZ() {
        return blockEntity.getPos().getZ();
    }

    @Override
    public BlockData getBlockData() {
        return FabricAdapter.adapt(blockEntity.getCachedState());
    }

    @Override
    public boolean update(boolean applyPhysics) {
        return true;
    }
}
