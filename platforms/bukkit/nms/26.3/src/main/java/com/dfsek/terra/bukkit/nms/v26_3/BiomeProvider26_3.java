package com.dfsek.terra.bukkit.nms.v26_3;

import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.bukkit.nms.NMSBiomeProvider;


public final class BiomeProvider26_3 extends NMSBiomeProvider {
    public BiomeProvider26_3(BiomeProvider delegate, long seed) {
        super(delegate, seed);
    }

    @Override
    public @NotNull BiomeResolver createResolver(@NotNull Sampler sampler) {
        return (x, y, z) -> getNoiseBiome(x, y, z, sampler);
    }
}
