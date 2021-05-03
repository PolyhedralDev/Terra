package com.dfsek.terra.fabric.world.block.state;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.fabric.world.FabricAdapter;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
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
        WorldAccess worldAccess = (WorldAccess) block.getLocation().getWorld();

        BlockEntity entity = worldAccess.getBlockEntity(FabricAdapter.adapt(block.getLocation().toVector()));
        if(entity instanceof SignBlockEntity) {
            return new FabricSign((SignBlockEntity) entity, worldAccess);
        } else if(entity instanceof MobSpawnerBlockEntity) {
            return new FabricMobSpawner((MobSpawnerBlockEntity) entity, worldAccess);
        } else if(entity instanceof LootableContainerBlockEntity) {
            return new FabricContainer((LootableContainerBlockEntity) entity, worldAccess);
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
