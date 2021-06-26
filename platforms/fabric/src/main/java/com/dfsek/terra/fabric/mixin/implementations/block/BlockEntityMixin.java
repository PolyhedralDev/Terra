package com.dfsek.terra.fabric.mixin.implementations.block;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.fabric.util.FabricAdapter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.block.entity.BlockEntity.class)
@Implements(@Interface(iface = BlockEntity.class, prefix = "terra$", remap = Interface.Remap.NONE))
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

    public int terra$getX() {
        return pos.getX();
    }

    public int terra$getY() {
        return pos.getY();
    }

    public int terra$getZ() {
        return pos.getZ();
    }

    public BlockState terra$getBlockData() {
        return FabricAdapter.adapt(getCachedState());
    }

    public boolean terra$update(boolean applyPhysics) {
        if(hasWorld()) world.getChunk(pos).setBlockEntity((net.minecraft.block.entity.BlockEntity) (Object) this);
        return true;
    }
}
