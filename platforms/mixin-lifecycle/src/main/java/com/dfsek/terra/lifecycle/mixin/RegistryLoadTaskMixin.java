package com.dfsek.terra.lifecycle.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryLoadTask;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

import com.dfsek.terra.lifecycle.util.LoadContextHolder;


@Mixin(RegistryLoadTask.class)
public abstract class RegistryLoadTaskMixin implements LoadContextHolder {
    @Unique
    private @Nullable List<HolderLookup.RegistryLookup<?>> terra$contextRegistries;

    @Override
    public @Nullable List<HolderLookup.RegistryLookup<?>> terra_contextRegistries() {
        return terra$contextRegistries;
    }

    @Override
    public void terra_setContextRegistries(List<HolderLookup.RegistryLookup<?>> contextRegistries) {
        this.terra$contextRegistries = contextRegistries;
    }
}
