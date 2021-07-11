package com.dfsek.terra.forge.mixin.implementations.block;

import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.entity.BlockState;
import com.dfsek.terra.forge.ForgeAdapter;
import com.dfsek.terra.forge.block.ForgeBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(TileEntity.class)
@Implements(@Interface(iface = BlockState.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class TileEntityMixin {
    @Shadow
    protected BlockPos worldPosition;

    @Shadow
    @Nullable
    protected World level;

    @Shadow
    @Nullable
    private net.minecraft.block.BlockState blockState;

    @Shadow
    public abstract boolean hasLevel();

    public Object terra$getHandle() {
        return this;
    }

    public Block terra$getBlock() {
        return new ForgeBlock(worldPosition, level);
    }

    public int terra$getX() {
        return worldPosition.getX();
    }

    public int terra$getY() {
        return worldPosition.getY();
    }

    public int terra$getZ() {
        return worldPosition.getZ();
    }

    public BlockData terra$getBlockData() {
        return ForgeAdapter.adapt(blockState);
    }

    public boolean terra$update(boolean applyPhysics) {
        if(hasLevel()) level.getChunk(worldPosition).setBlockEntity(worldPosition, (TileEntity) (Object) this);
        return true;
    }
}
