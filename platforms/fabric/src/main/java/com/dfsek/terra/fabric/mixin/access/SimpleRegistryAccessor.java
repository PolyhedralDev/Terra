package com.dfsek.terra.fabric.mixin.access;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleRegistry.class)
public interface SimpleRegistryAccessor<T> {
    @Accessor("rawIdToEntry")
    ObjectList<T> getRawIdToEntry();
}
