package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.bukkit.world.BiomeHandler;
import com.dfsek.terra.bukkit.world.BukkitBiomeInfo;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import dev.wyck.biome.Biome;
import dev.wyck.worldgen.biome.custom.BiomeSourceContext;
import dev.wyck.worldgen.biome.custom.CustomBiomeSource;

import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class WyckBiomeProvider extends CustomBiomeSource {
    private final BiomeProvider delegate;
    private final long seed;

    public WyckBiomeProvider(BiomeProvider delegate, long seed) {
        super();
        this.delegate = delegate;
        this.seed = seed;
    }

    @Override
    public Biome biome(BiomeSourceContext context) {
        return BiomeHandler.getBiomeOrThrow(((BukkitPlatformBiome) delegate.getBiome(context.blockX() << 2, context.blockY() << 2, context.blockZ() << 2, seed)
            .getPlatformBiome()).getContext()
            .get(BukkitBiomeInfo.class)
            .biomeKey());
    }

    @Override
    public Set<Biome> possibleBiomes() {
        return delegate.stream()
            .map(biome -> BiomeHandler.getBiomeOrThrow(((BukkitPlatformBiome) biome.getPlatformBiome())
                .getContext()
                .get(BukkitBiomeInfo.class)
                .biomeKey()))
            .collect(Collectors.toSet());
    }
}
