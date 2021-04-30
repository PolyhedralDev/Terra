package com.dfsek.terra.forge.world.block.state;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.forge.world.ForgeAdapter;
import com.dfsek.terra.forge.world.block.ForgeBlock;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldHandle;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorld;

public class ForgeBlockState implements BlockState {
    protected final TileEntity blockEntity;
    private final IWorld worldAccess;

    public ForgeBlockState(TileEntity blockEntity, IWorld worldAccess) {
        this.blockEntity = blockEntity;
        this.worldAccess = worldAccess;
    }

    public static ForgeBlockState newInstance(Block block) {
        IWorld worldAccess = ((ForgeWorldHandle) block.getLocation().getWorld()).getWorld();

        TileEntity entity = worldAccess.getBlockEntity(ForgeAdapter.adapt(block.getLocation().toVector()));
        if(entity instanceof SignTileEntity) {
            return new ForgeSign((SignTileEntity) entity, worldAccess);
        } else if(entity instanceof MobSpawnerTileEntity) {
            return new ForgeMobSpawner((MobSpawnerTileEntity) entity, worldAccess);
        } else if(entity instanceof LockableLootTileEntity) {
            return new ForgeContainer((LockableLootTileEntity) entity, worldAccess);
        }
        return null;
    }

    @Override
    public TileEntity getHandle() {
        return blockEntity;
    }

    @Override
    public Block getBlock() {
        return new ForgeBlock(blockEntity.getBlockPos(), blockEntity.getLevel());
    }

    @Override
    public int getX() {
        return blockEntity.getBlockPos().getX();
    }

    @Override
    public int getY() {
        return blockEntity.getBlockPos().getY();
    }

    @Override
    public int getZ() {
        return blockEntity.getBlockPos().getZ();
    }

    @Override
    public BlockData getBlockData() {
        return ForgeAdapter.adapt(blockEntity.getBlockState());
    }

    @Override
    public boolean update(boolean applyPhysics) {
        worldAccess.getChunk(blockEntity.getBlockPos()).setBlockEntity(blockEntity.getBlockPos(), blockEntity);
        return true;
    }
}
