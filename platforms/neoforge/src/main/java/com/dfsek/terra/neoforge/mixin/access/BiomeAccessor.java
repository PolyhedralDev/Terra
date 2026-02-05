package com.dfsek.terra.neoforge.mixin.access;

import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(Biome.class)
public interface BiomeAccessor extends com.dfsek.terra.mod.mixin.access.BiomeAccessor {
    @Accessor("weather")
    Biome.Weather getWeather();
}
