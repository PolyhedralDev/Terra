package com.dfsek.terra.forge.mixin.implementations.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.forge.ForgeAdapter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
@Implements(@Interface(iface = BlockType.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class BlockMixin {
    @Shadow
    private BlockState defaultBlockState;

    public Object terra$getHandle() {
        return this;
    }

    public BlockData terra$getDefaultData() {
        return ForgeAdapter.adapt(defaultBlockState);
    }

    public boolean terra$isSolid() {
        return defaultBlockState.canOcclude();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean terra$isWater() {
        return ((Object) this) == Blocks.WATER;
    }
}
