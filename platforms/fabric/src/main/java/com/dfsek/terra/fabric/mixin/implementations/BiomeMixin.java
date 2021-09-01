package com.dfsek.terra.fabric.mixin.implementations;

import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(Biome.class)
@Implements(@Interface(iface = com.dfsek.terra.api.world.biome.Biome.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class BiomeMixin {
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
}
