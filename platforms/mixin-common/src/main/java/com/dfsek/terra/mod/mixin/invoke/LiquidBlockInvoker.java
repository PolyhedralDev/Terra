package com.dfsek.terra.mod.mixin.invoke;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(LiquidBlock.class)
public interface LiquidBlockInvoker {
    @Invoker("getFluidState")
    FluidState invokeGetFluidState(BlockState state);
}
