package com.dfsek.terra.bukkit.nms.v1_18_R2;

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
    private final BiomeSource vanilla;
    private final long seed;
    private final Registry<Biome> biomeRegistry = Registries.biomeRegistry();
    
    public NMSBiomeProvider(BiomeProvider delegate, BiomeSource vanilla, long seed) {
        super(delegate.stream()
                      .map(biome -> Registries.biomeRegistry()
                                              .getOrCreateHolder(((BukkitPlatformBiome) biome.getPlatformBiome()).getContext()
                                                                                                                 .get(NMSBiomeInfo.class)
                                                                                                                 .biomeKey())));
        this.delegate = delegate;
        this.vanilla = vanilla;
        this.seed = seed;
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return BiomeSource.CODEC;
    }
    
    @Override
    public @NotNull BiomeSource withSeed(long seed) {
        return new NMSBiomeProvider(delegate, vanilla, seed);
    }
    
    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int x, int y, int z, @NotNull Sampler sampler) {
        return biomeRegistry.getOrCreateHolder(((BukkitPlatformBiome) delegate.getBiome(x << 2, y << 2, z << 2, seed).getPlatformBiome())
                                                       .getContext()
                                                       .get(NMSBiomeInfo.class)
                                                       .biomeKey());
    }
}
