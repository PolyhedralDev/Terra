package com.dfsek.terra.mod.mixin.invoke;

import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(Biome.class)
public interface BiomeInvoker {
    @Invoker("getGrassColorFromTexture")
    int invokeGetDefaultGrassColor();
}
