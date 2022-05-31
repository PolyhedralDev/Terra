package com.dfsek.terra.bukkit.nms;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.levelgen.ChunkGeneratorAbstract;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.WorldGenStage;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class NMSChunkGeneratorDelegate extends ChunkGenerator {
    private final NMSBiomeProvider biomeSource;
    private final com.dfsek.terra.api.world.chunk.generation.ChunkGenerator delegate;
    
    private final ChunkGenerator vanilla;
    private final ConfigPack pack;
    
    private final long seed;
    
    public static IRegistry<StructureSet> getStructureRegistry() {
        CraftServer craftserver = (CraftServer) Bukkit.getServer();
        DedicatedServer dedicatedserver = craftserver.getServer();
        
        return dedicatedserver
                .aU() // getRegistryManager
                .b( // getRegistry
                    IRegistry.aM // biome registry key
                  );
    }
    
    
    public NMSChunkGeneratorDelegate(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, long seed) {
        super(getStructureRegistry(), Optional.empty(), biomeProvider, biomeProvider, seed);
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        this.vanilla = vanilla;
        this.biomeSource = biomeProvider;
        this.pack = pack;
        this.seed = seed;
    }
    
    @Override //applyCarvers
    public void a(RegionLimitedWorldAccess regionlimitedworldaccess, long var2, BiomeManager var4, StructureManager var5,
                  IChunkAccess ichunkaccess, WorldGenStage.Features var7) {
        // no-op
    }
    
    @Override // getSeaLevel
    public int g() {
        return vanilla.g();
    }
    
    @Override //fillFromNoise
    public CompletableFuture<IChunkAccess> a(Executor executor, Blender blender, StructureManager structuremanager,
                                             IChunkAccess ichunkaccess) {
        return vanilla.a(executor, blender, structuremanager, ichunkaccess);
    }
    
    
    @Override //buildSurface. Used to be buildBase
    public void a(RegionLimitedWorldAccess regionlimitedworldaccess, StructureManager structuremanager, IChunkAccess ichunkaccess) {
    
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> b() {
        return ChunkGeneratorAbstract.a;
    }
    
    @Override // getColumn
    public BlockColumn a(int x, int z, LevelHeightAccessor height) {
        IBlockData[] array = new IBlockData[height.v_()];
        WorldProperties properties = new NMSWorldProperties(seed, height);
        BiomeProvider biomeProvider = pack.getBiomeProvider().caching(properties);
        for(int y = properties.getMaxHeight() - 1; y >= properties.getMinHeight(); y--) {
            array[y - properties.getMinHeight()] = ((CraftBlockData) delegate.getBlock(properties, x, y, z, biomeProvider)
                                                                             .getHandle()).getState();
        }
        return new BlockColumn(getMinimumY(), array);
    }
    
    @Override // withSeed
    public ChunkGenerator a(long seed) {
        return new NMSChunkGeneratorDelegate(vanilla, pack, biomeSource, seed);
    }
    
    //spawnOriginalMobs
    public void a(RegionLimitedWorldAccess regionlimitedworldaccess) {
        vanilla.a(regionlimitedworldaccess);
    }
    
    // getGenDepth
    public int f() {
        return vanilla.f();
    }
    
    // climateSampler
    public Sampler d() {
        return Climate.a();
    }
    
    //getMinY
    @Override
    public int h() {
        return vanilla.h();
    }
    
    @Override // getBaseHeight
    public int a(int x, int z, HeightMap.Type heightmap, LevelHeightAccessor height) {
        WorldProperties properties = new NMSWorldProperties(seed, height);
        int y = properties.getMaxHeight();
        BiomeProvider biomeProvider = pack.getBiomeProvider().caching(properties);
        while(y >= getMinimumY() && !heightmap.e().test(
                ((CraftBlockData) delegate.getBlock(properties, x, y - 1, z, biomeProvider).getHandle()).getState())) {
            y--;
        }
        return y;
    }
    
    public int getMinimumY() {
        return h();
    }
    
    @Override //addDebugScreenInfo
    public void a(List<String> arg0, BlockPosition arg1) {
    
    }
}
