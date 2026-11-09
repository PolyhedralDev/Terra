package com.dfsek.terra.lifecycle.util;

import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface LoadContextHolder {
    @Nullable List<HolderLookup.RegistryLookup<?>> terra_contextRegistries();

    void terra_setContextRegistries(List<HolderLookup.RegistryLookup<?>> contextRegistries);
}
