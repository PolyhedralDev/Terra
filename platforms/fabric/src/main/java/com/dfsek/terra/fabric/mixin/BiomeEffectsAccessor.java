package com.dfsek.terra.fabric.mixin;

import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(BiomeEffects.class)
public interface BiomeEffectsAccessor {
    @Accessor
    int getFogColor();

    @Accessor
    int getWaterColor();

    @Accessor
    int getWaterFogColor();

    @Accessor
    int getSkyColor();

    @Accessor
    Optional<Integer> getFoliageColor();

    @Accessor
    Optional<Integer> getGrassColor();

    @Accessor
    BiomeEffects.GrassColorModifier getGrassColorModifier();
}
