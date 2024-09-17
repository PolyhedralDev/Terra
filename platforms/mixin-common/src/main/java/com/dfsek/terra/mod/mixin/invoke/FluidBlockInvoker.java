package com.dfsek.terra.mod.mixin.invoke;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(FluidBlock.class)
public interface FluidBlockInvoker {
    @Invoker("getFluidState")
    public FluidState invokeGetFluidState(BlockState state);
}
