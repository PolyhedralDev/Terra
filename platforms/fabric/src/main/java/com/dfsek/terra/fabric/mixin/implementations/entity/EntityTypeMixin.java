package com.dfsek.terra.fabric.mixin.implementations.entity;

import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityType.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.entity.EntityType.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class EntityTypeMixin {
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
}
