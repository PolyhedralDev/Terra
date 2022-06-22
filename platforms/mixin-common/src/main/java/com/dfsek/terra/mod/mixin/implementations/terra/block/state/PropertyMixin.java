package com.dfsek.terra.mod.mixin.implementations.terra.block.state;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

import com.dfsek.terra.api.block.state.properties.Property;


@Mixin(net.minecraft.state.property.Property.class)
@Implements(@Interface(iface = Property.class, prefix = "terra$", remap = Remap.NONE))
public abstract class PropertyMixin<T> {
    @Shadow
    @Final
    private Class<T> type;
    @Shadow
    @Final
    private String name;
    
    @Shadow
    public abstract Collection<T> getValues();
    
    @Intrinsic
    public Collection<T> terra$values() {
        return getValues();
    }
    
    @Intrinsic
    public Class<T> terra$getType() {
        return type;
    }
    
    @Intrinsic
    public String terra$getID() {
        return name;
    }
}
