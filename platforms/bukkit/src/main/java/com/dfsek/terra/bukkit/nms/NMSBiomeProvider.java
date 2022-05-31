package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.biome.WorldChunkManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;


public class NMSBiomeProvider extends WorldChunkManager {
    private final BiomeProvider delegate;
    private final WorldChunkManager vanilla;
    private final long seed;
    
    private static final Lazy<IRegistry<BiomeBase>> biomeRegistry = Lazy.lazy(() -> {
        DedicatedServer dedicatedserver = ((CraftServer) Bukkit.getServer()).getServer();
        return dedicatedserver.aU().b(IRegistry.aP);
    });
    
    public NMSBiomeProvider(BiomeProvider delegate, WorldChunkManager vanilla, long seed) {
        super(delegate.stream().map(biome -> biomeRegistry.value().g(((BukkitPlatformBiome) biome.getPlatformBiome()).getResourceKey())));
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
        //return CraftBlock.biomeToBiomeBase(biomeRegistry.value(), ((BukkitPlatformBiome) delegate.getBiome(x << 2, y << 2, z << 2, seed).getPlatformBiome()).getHandle());
        return biomeRegistry.value().g(((BukkitPlatformBiome) delegate.getBiome(x << 2, y << 2, z << 2, seed).getPlatformBiome()).getResourceKey());
    }
}
