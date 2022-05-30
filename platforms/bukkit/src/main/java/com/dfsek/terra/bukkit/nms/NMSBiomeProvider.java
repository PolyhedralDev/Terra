package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.biome.WorldChunkManager;


public class NMSBiomeProvider extends WorldChunkManager {
    private final BiomeProvider delegate;
    private final WorldChunkManager vanilla;
    private final long seed;
    
    public NMSBiomeProvider(BiomeProvider delegate, WorldChunkManager vanilla, long seed) {
        super(vanilla.b().stream());
        this.delegate = delegate;
        this.vanilla = vanilla;
        this.seed = seed;
    }
    
    @Override
    protected Codec<? extends WorldChunkManager> a() {
        return WorldChunkManager.a;
    }
    
    @Override
    public WorldChunkManager a(long seed) {
        return withSeed(seed);
    }
    
    public WorldChunkManager withSeed(long seed) {
        return new NMSBiomeProvider(delegate, vanilla, seed);
    }
    
    @Override
    public Holder<BiomeBase> getNoiseBiome(int x, int y, int z, Sampler sampler) {
        return ((BukkitPlatformBiome) delegate.getBiome(x << 2, y << 2, z << 2, seed).getPlatformBiome()).getResourceKey();
    }
}
