package com.dfsek.terra.fabric.mixin.implementations.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
@Implements(@Interface(iface = BlockType.class, prefix = "terra$"))
public abstract class BlockMixin {
    @Shadow
    private BlockState defaultState;

    public Object terra$getHandle() {
        return this;
    }

    public BlockData terra$getDefaultData() {
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
