package com.dfsek.terra.bukkit.nms.v1_18_R2;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.SectionPosition;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeBase;
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
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureManager;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


public class NMSChunkGeneratorDelegate extends ChunkGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSChunkGeneratorDelegate.class);
    private final NMSBiomeProvider biomeSource;
    private final com.dfsek.terra.api.world.chunk.generation.ChunkGenerator delegate;
    
    private final ChunkGenerator vanilla;
    private final ConfigPack pack;
    
    private final long seed;
    
    private final Map<ConcentricRingsStructurePlacement, Lazy<List<ChunkCoordIntPair>>> h = new Object2ObjectArrayMap<>();
    private static final Lazy<List<ChunkCoordIntPair>> EMPTY = Lazy.lazy(List::of);
    
    
    public NMSChunkGeneratorDelegate(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, long seed) {
        super(Registries.structureSet(), Optional.empty(), biomeProvider, biomeProvider, seed);
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
    
    @Override
    public void a(IRegistryCustom iregistrycustom, StructureManager structuremanager, IChunkAccess ichunkaccess,
                  DefinedStructureManager definedstructuremanager, long i) {
        super.a(iregistrycustom, structuremanager, ichunkaccess, definedstructuremanager, i);
    }
    
    @Nullable
    @Override
    public List<ChunkCoordIntPair> a(ConcentricRingsStructurePlacement concentricringsstructureplacement) {
        this.i();
        return this.h.getOrDefault(concentricringsstructureplacement, EMPTY).value();
    }
    
    private volatile boolean rings = false;
    
    @Override
    public synchronized void i() {
        if(!this.rings) {
            super.i();
            this.populateStrongholdData();
            this.rings = true;
        }
    }
    
    private void populateStrongholdData() {
        LOGGER.info("Generating safe stronghold data. This may take up to a minute.");
        Set<Holder<BiomeBase>> set = this.d.b();
        a().map(h -> h.a()).forEach((holder) -> { // we dont need the spigot crap because it doesnt touch concentric.
            StructurePlacement structureplacement = holder.b();
            if(structureplacement instanceof ConcentricRingsStructurePlacement concentricringsstructureplacement) {
                if(holder.a().stream().anyMatch((structureset_a1) -> structureset_a1.a(set::contains))) {
                    this.h.put(concentricringsstructureplacement,
                               Lazy.lazy(() -> this.generateRingPositions(holder, concentricringsstructureplacement)));
                }
            }
        });
    }
    
    private List<ChunkCoordIntPair> generateRingPositions(StructureSet holder,
                                                          ConcentricRingsStructurePlacement concentricringsstructureplacement) {
        if(concentricringsstructureplacement.d() == 0) {
            return List.of();
        }
        List<ChunkCoordIntPair> list = new ArrayList<>();
        Set<Holder<BiomeBase>> set = holder.a().stream().flatMap((structureset_a) -> (structureset_a.a().a()).a().a()).collect(
                Collectors.toSet());
        int i = concentricringsstructureplacement.b();
        int j = concentricringsstructureplacement.d();
        int k = concentricringsstructureplacement.c();
        Random random = new Random();
        random.setSeed(this.j);
        double d0 = random.nextDouble() * Math.PI * 2.0;
        int l = 0;
        int i1 = 0;
        
        for(int j1 = 0; j1 < j; ++j1) {
            double d1 = (double) (4 * i + i * i1 * 6) + (random.nextDouble() - 0.5) * (double) i * 2.5;
            int k1 = (int) Math.round(Math.cos(d0) * d1);
            int l1 = (int) Math.round(Math.sin(d0) * d1);
            int i2 = SectionPosition.a(k1, 8);
            int j2 = SectionPosition.a(l1, 8);
            Objects.requireNonNull(set);
            Pair<BlockPosition, Holder<BiomeBase>> pair = this.c.a(i2, 0, j2, 112, set::contains, random, this.d());
            if(pair != null) {
                BlockPosition blockposition = pair.getFirst();
                k1 = SectionPosition.a(blockposition.u());
                l1 = SectionPosition.a(blockposition.w());
            }
            
            list.add(new ChunkCoordIntPair(k1, l1));
            d0 += 6.283185307179586 / (double) k;
            ++l;
            if(l == k) {
                ++i1;
                l = 0;
                k += 2 * k / (i1 + 1);
                k = Math.min(k, j - j1);
                d0 += random.nextDouble() * Math.PI * 2.0;
            }
        }
        return list;
        
    }
    
    public int getMinimumY() {
        return h();
    }
    
    @Override //addDebugScreenInfo
    public void a(List<String> arg0, BlockPosition arg1) {
    
    }
}
