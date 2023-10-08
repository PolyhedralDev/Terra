package com.dfsek.terra.lifecycle.mixin;

import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

import com.dfsek.terra.lifecycle.util.RegistryHack;


@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T> implements RegistryHack {
    @Shadow
    @Final
    private Map<T, Reference<T>> valueToEntry;
    
    @Override
    public void terra_bind() {
        valueToEntry.forEach((value, entry) -> {
            //noinspection unchecked
            ((RegistryEntryReferenceInvoker<T>) entry).invokeSetValue(value);
        });
    }
}
