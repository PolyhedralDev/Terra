package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.Function;


@Mixin(State.class)
public interface StateAccessor {
    @Accessor("PROPERTY_MAP_PRINTER")
    static Function<Map.Entry<Property<?>, Comparable<?>>, String> getPropertyMapPrinter() {
        throw new UnsupportedOperationException();
    }
}
