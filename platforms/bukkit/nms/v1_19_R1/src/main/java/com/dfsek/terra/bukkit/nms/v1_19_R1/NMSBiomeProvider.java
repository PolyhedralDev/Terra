package com.dfsek.terra.bukkit.nms.v1_19_R1;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.checkerframework.checker.nullness.qual.NonNull;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.ProtoPlatformBiome;


public class NMSBiomeProvider extends BiomeSource {
    private final BiomeProvider delegate;
    private final long seed;
    private final Registry<Biome> biomeRegistry = Registries.biomeRegistry();
    
    public NMSBiomeProvider(BiomeProvider delegate, long seed) {
        super(delegate.stream()
                      .map(biome -> Registries.biomeRegistry()
                                              .getHolderOrThrow(((ProtoPlatformBiome) biome.getPlatformBiome()).getBiome())));
        this.delegate = delegate;
        this.seed = seed;
    }
    
    @Override
    protected @NonNull Codec<? extends BiomeSource> codec() {
        return BiomeSource.CODEC;
    }
    
    @Override
    public @NonNull Holder<Biome> getNoiseBiome(int x, int y, int z, @NonNull Sampler sampler) {
        return biomeRegistry.getHolderOrThrow(((ProtoPlatformBiome) delegate.getBiome(x << 2, y << 2, z << 2, seed)
                                                                            .getPlatformBiome()).getBiome());
    }
}
