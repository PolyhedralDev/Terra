package com.dfsek.terra.lifecycle.mixin;

import net.minecraft.core.Holder.Reference;
import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

import com.dfsek.terra.lifecycle.util.RegistryHack;


@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> implements RegistryHack {
    @Shadow
    @Final
    private Map<T, Reference<T>> byValue;

    @Override
    public void terra_bind() {
        byValue.forEach((value, entry) -> {
            //noinspection unchecked
            ((HolderReferenceInvoker<T>) entry).invokeSetValue(value);
        });
    }
}
