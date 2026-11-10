package com.dfsek.terra.lifecycle.mixin;


import net.minecraft.core.Holder.Reference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(Reference.class)
public interface HolderReferenceInvoker<T> {
    @Invoker("bindValue")
    void invokeSetValue(T value);
}
