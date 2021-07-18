package com.dfsek.terra.fabric.mixin.implementations.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.fabric.util.FabricAdapter;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
@Implements(@Interface(iface = BlockType.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class BlockMixin {
    @Shadow
    private net.minecraft.block.BlockState defaultState;

    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }

    public BlockState terra$getDefaultData() {
        return FabricAdapter.adapt(defaultState);
    }

    public boolean terra$isSolid() {
        return defaultState.isOpaque();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean terra$isWater() {
        return ((Object) this) == Blocks.WATER;
    }
}
