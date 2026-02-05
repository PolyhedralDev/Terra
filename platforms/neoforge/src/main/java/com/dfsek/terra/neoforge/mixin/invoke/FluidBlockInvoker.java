package com.dfsek.terra.neoforge.mixin.invoke;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(FluidBlock.class)
public interface FluidBlockInvoker extends com.dfsek.terra.mod.mixin.invoke.FluidBlockInvoker {
    @Invoker("getFluidState")
    public FluidState invokeGetFluidState(BlockState state);
}
