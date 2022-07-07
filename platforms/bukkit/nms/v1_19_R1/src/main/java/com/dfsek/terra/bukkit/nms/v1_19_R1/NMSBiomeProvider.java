package com.dfsek.terra.bukkit.nms.v1_19_R1;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;


public class NMSBiomeProvider extends BiomeSource {
    private final BiomeProvider delegate;
    private final long seed;
    private final Registry<Biome> biomeRegistry = Registries.biomeRegistry();
    
    public NMSBiomeProvider(BiomeProvider delegate, long seed) {
        super(delegate.stream()
                      .map(biome -> Registries.biomeRegistry()
                                              .getHolderOrThrow(((BukkitPlatformBiome) biome.getPlatformBiome()).getContext()
                                                                                                                .get(NMSBiomeInfo.class)
                                                                                                                .biomeKey())));
        this.delegate = delegate;
        this.seed = seed;
    }
    
    @Override
    protected @NotNull Codec<? extends BiomeSource> codec() {
        return BiomeSource.CODEC;
    }
    
    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int x, int y, int z, @NotNull Sampler sampler) {
        return biomeRegistry.getHolderOrThrow(((BukkitPlatformBiome) delegate.getBiome(x << 2, y << 2, z << 2, seed)
                                                                             .getPlatformBiome()).getContext()
                                                                                                 .get(NMSBiomeInfo.class)
                                                                                                 .biomeKey());
    }
}
