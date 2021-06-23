package com.dfsek.terra.fabric.mixin.implementations.block;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.fabric.block.FabricBlock;
import com.dfsek.terra.fabric.util.FabricAdapter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockEntity.class)
@Implements(@Interface(iface = BlockState.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class BlockEntityMixin {
    @Final
    @Shadow
    protected BlockPos pos;
    @Shadow
    @Nullable
    protected World world;

    @Shadow
    public abstract net.minecraft.block.BlockState getCachedState();

    @Shadow
    public abstract boolean hasWorld();

    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }

    public Block terra$getBlock() {
        return new FabricBlock(pos, world);
    }

    public int terra$getX() {
        return pos.getX();
    }

    public int terra$getY() {
        return pos.getY();
    }

    public int terra$getZ() {
        return pos.getZ();
    }

    public BlockData terra$getBlockData() {
        return FabricAdapter.adapt(getCachedState());
    }

    public boolean terra$update(boolean applyPhysics) {
        if(hasWorld()) world.getChunk(pos).setBlockEntity((BlockEntity) (Object) this);
        return true;
    }
}
