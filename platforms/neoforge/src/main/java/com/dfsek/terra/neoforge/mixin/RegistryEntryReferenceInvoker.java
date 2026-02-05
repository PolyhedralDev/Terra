package com.dfsek.terra.neoforge.mixin;


import net.minecraft.registry.entry.RegistryEntry.Reference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(Reference.class)
public interface RegistryEntryReferenceInvoker<T> {
    @Invoker("setValue")
    void invokeSetValue(T value);
}
