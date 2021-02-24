package com.dfsek.terra.fabric.world.block.state;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.fabric.world.FabricAdapter;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldHandle;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.world.WorldAccess;

public class FabricBlockState implements BlockState {
    protected final BlockEntity blockEntity;
    private final WorldAccess worldAccess;

    public FabricBlockState(BlockEntity blockEntity, WorldAccess worldAccess) {
        this.blockEntity = blockEntity;
        this.worldAccess = worldAccess;
    }

    public static FabricBlockState newInstance(Block block) {
        net.minecraft.block.Block block1 = ((FabricBlockData) block.getBlockData()).getHandle().getBlock();
        WorldAccess worldAccess = ((FabricWorldHandle) block.getLocation().getWorld()).getWorld();

        if(block1 instanceof AbstractSignBlock) {
            SignBlockEntity signBlockEntity = (SignBlockEntity) worldAccess.getBlockEntity(FabricAdapter.adapt(block.getLocation().toVector()));
            return new FabricSign(signBlockEntity, worldAccess);
        } else if(block1 instanceof SpawnerBlock) {
            MobSpawnerBlockEntity mobSpawnerBlockEntity = (MobSpawnerBlockEntity) worldAccess.getBlockEntity(FabricAdapter.adapt(block.getLocation().toVector()));
            return new FabricMobSpawner(mobSpawnerBlockEntity, worldAccess);
        } else if(block1 instanceof AbstractChestBlock) {
            BlockEntity abstractChestBlock = worldAccess.getBlockEntity(FabricAdapter.adapt(block.getLocation().toVector()));
            System.out.println("inventory: " + block1);
            return new FabricContainer(abstractChestBlock, worldAccess);
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
        worldAccess.getChunk(blockEntity.getPos()).setBlockEntity(blockEntity.getPos(), blockEntity);
        return true;
    }
}
