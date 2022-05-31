package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.levelgen.ChunkGeneratorAbstract;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.WorldGenStage;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class NMSChunkGeneratorDelegate extends ChunkGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkGenerator.class);
    private final NMSBiomeProvider biomeSource;
    private final com.dfsek.terra.api.world.chunk.generation.ChunkGenerator delegate;
    
    private final ChunkGenerator vanilla;
    private final ConfigPack pack;
    private final CraftWorld world;
    
    private volatile boolean structures = false;
    
    
    public NMSChunkGeneratorDelegate(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, CraftWorld world) {
        super(vanilla.b, vanilla.e, biomeProvider, biomeProvider, world.getSeed());
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        this.vanilla = vanilla;
        this.biomeSource = biomeProvider;
        this.pack = pack;
        this.world = world;
    }
    
    public void enableStructures() {
        if(structures) {
            throw new IllegalStateException("Structures have already been enabled!");
        }
        LOGGER.info("Enabling structure generation...");
        this.structures = true;
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
    public void a(GeneratorAccessSeed gas, StructureManager manager, IChunkAccess ica) {
        if(structures) {
            vanilla.a(gas, manager, ica);
        }
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> b() {
        return ChunkGeneratorAbstract.a;
    }
    
    @Override // getColumn
    public BlockColumn a(int var0, int var1, LevelHeightAccessor var2) {
        return vanilla.a(var0, var1, var2);
    }
    
    @Override // withSeed
    public ChunkGenerator a(long seed) {
        return new NMSChunkGeneratorDelegate(vanilla, pack, biomeSource, world);
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
    
    @Override //getFirstFreeHeight
    public int b(int i, int j, HeightMap.Type heightmap_type, LevelHeightAccessor levelheightaccessor) {
        return this.a(i, j, heightmap_type, levelheightaccessor);
    }
    
    
    @Override //getFirstOccupiedHeight
    public int c(int i, int j, HeightMap.Type heightmap_type, LevelHeightAccessor levelheightaccessor) {
        return this.a(i, j, heightmap_type, levelheightaccessor) - 1;
    }
    
    @Override // getBaseHeight
    public int a(int x, int z, HeightMap.Type heightmap, LevelHeightAccessor height) {
        int y = world.getMaxHeight();
        WorldProperties properties = BukkitAdapter.adapt(world);
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
