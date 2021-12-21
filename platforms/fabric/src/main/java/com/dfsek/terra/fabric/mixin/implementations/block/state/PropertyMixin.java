package com.dfsek.terra.fabric.mixin.implementations.block.state;

import com.dfsek.terra.api.block.state.properties.Property;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;


@Mixin(net.minecraft.state.property.Property.class)
public abstract class PropertyMixin<T> implements Property<T> {
    @Shadow
    @Final
    private Class<T> type;
    
    @Shadow
    public abstract Collection<T> getValues();
    
    @Shadow
    @Final
    private String name;
    
    @Override
    public Collection<T> values() {
        return getValues();
    }
    
    @Override
    public Class<T> getType() {
        return type;
    }
    
    @Override
    public String getName() {
        return name;
    }
}
