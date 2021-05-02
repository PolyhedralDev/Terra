package com.dfsek.terra.forge.mixin;

import net.minecraft.world.biome.BiomeAmbience;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(BiomeAmbience.class)
public interface BiomeAmbienceAccessor {
    @Accessor
    int getFogColor();

    @Accessor
    int getWaterColor();

    @Accessor
    int getWaterFogColor();

    @Accessor
    int getSkyColor();

    @Accessor
    Optional<Integer> getFoliageColorOverride();

    @Accessor
    Optional<Integer> getGrassColorOverride();

    @Accessor
    BiomeAmbience.GrassColorModifier getGrassColorModifier();
}
