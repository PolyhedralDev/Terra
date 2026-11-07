package com.dfsek.terra.lifecycle.mixin;

import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryLoadTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(RegistryLoadTask.class)
public interface RegistryLoadTaskAccessor {
    @Accessor("registry")
    WritableRegistry<?> getRegistry();
}
