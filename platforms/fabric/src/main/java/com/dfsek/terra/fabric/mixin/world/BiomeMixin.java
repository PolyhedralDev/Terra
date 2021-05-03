package com.dfsek.terra.fabric.mixin.world;

import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Biome.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.world.Biome.class, prefix = "terra$"))
public abstract class BiomeMixin {
    public Object terra$getHandle() {
        return this;
    }
}
