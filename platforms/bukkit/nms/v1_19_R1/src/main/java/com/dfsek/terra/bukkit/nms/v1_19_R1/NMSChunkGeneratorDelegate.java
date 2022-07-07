package com.dfsek.terra.bukkit.nms.v1_19_R1;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import org.bukkit.craftbukkit.v1_19_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class NMSChunkGeneratorDelegate extends ChunkGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSChunkGeneratorDelegate.class);
    private final com.dfsek.terra.api.world.chunk.generation.ChunkGenerator delegate;
    
    private final ChunkGenerator vanilla;
    private final ConfigPack pack;
    
    private final long seed;
    private final Map<ConcentricRingsStructurePlacement, Lazy<List<ChunkPos>>> ringPositions = new Object2ObjectArrayMap<>();
    private volatile boolean rings = false;
    
    public NMSChunkGeneratorDelegate(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, long seed) {
        super(Registries.structureSet(), Optional.empty(), biomeProvider);
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        this.vanilla = vanilla;
        this.pack = pack;
        this.seed = seed;
    }
    
    @Override
    protected @NotNull Codec<? extends ChunkGenerator> codec() {
        return ChunkGenerator.CODEC;
    }
    
    @Override
    public void applyCarvers(@NotNull WorldGenRegion chunkRegion, long seed, @NotNull RandomState noiseConfig, @NotNull BiomeManager world,
                             @NotNull StructureManager structureAccessor, @NotNull ChunkAccess chunk, @NotNull Carving carverStep) {
        // no-op
    }
    
    @Override
    public void buildSurface(@NotNull WorldGenRegion region, @NotNull StructureManager structures, @NotNull RandomState noiseConfig,
                             @NotNull ChunkAccess chunk) {
        // no-op
    }
    
    @Override
    public void applyBiomeDecoration(@NotNull WorldGenLevel world, @NotNull ChunkAccess chunk,
                                     @NotNull StructureManager structureAccessor) {
        vanilla.applyBiomeDecoration(world, chunk, structureAccessor);
    }
    
    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion region) {
        vanilla.spawnOriginalMobs(region);
    }
    
    @Override
    public int getGenDepth() {
        return vanilla.getGenDepth();
    }
    
    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Executor executor, @NotNull Blender blender,
                                                                 @NotNull RandomState noiseConfig,
                                                                 @NotNull StructureManager structureAccessor, @NotNull ChunkAccess chunk) {
        return vanilla.fillFromNoise(executor, blender, noiseConfig, structureAccessor, chunk);
    }
    
    @Override
    public int getSeaLevel() {
        return vanilla.getSeaLevel();
    }
    
    @Override
    public int getMinY() {
        return vanilla.getMinY();
    }
    
    @Override
    public int getBaseHeight(int x, int z, @NotNull Types heightmap, @NotNull LevelHeightAccessor world, @NotNull RandomState noiseConfig) {
        WorldProperties properties = new NMSWorldProperties(seed, world);
        int y = properties.getMaxHeight();
        BiomeProvider biomeProvider = pack.getBiomeProvider();
        while(y >= getMinY() && !heightmap.isOpaque().test(
                ((CraftBlockData) delegate.getBlock(properties, x, y - 1, z, biomeProvider).getHandle()).getState())) {
            y--;
        }
        return y;
    }
    
    @Override
    public @NotNull NoiseColumn getBaseColumn(int x, int z, @NotNull LevelHeightAccessor world, @NotNull RandomState noiseConfig) {
        /*
        BlockState[] array = new BlockState[world.getHeight()];
        WorldProperties properties = new NMSWorldProperties(seed, world);
        BiomeProvider biomeProvider = pack.getBiomeProvider().caching(properties);
        for(int y = properties.getMaxHeight() - 1; y >= properties.getMinHeight(); y--) {
            array[y - properties.getMinHeight()] = ((CraftBlockData) delegate.getBlock(properties, x, y, z, biomeProvider)
                                                                             .getHandle()).getState();
        }
        return new NoiseColumn(getMinY(), array);

         */
        return vanilla.getBaseColumn(x, z, world, noiseConfig);
    }

    @Override
    public void addDebugScreenInfo(@NotNull List<String> text, @NotNull RandomState noiseConfig, @NotNull BlockPos pos) {

    }
    
    @Override
    public void ensureStructuresGenerated(@NotNull RandomState noiseConfig) {
        if(!this.rings) {
            super.ensureStructuresGenerated(noiseConfig);
            this.populateStrongholdData(noiseConfig);
            this.rings = true;
        }
        
    }
    
    @Override
    public List<ChunkPos> getRingPositionsFor(@NotNull ConcentricRingsStructurePlacement structurePlacement,
                                              @NotNull RandomState noiseConfig) {
        ensureStructuresGenerated(noiseConfig);
        return ringPositions.get(structurePlacement).value();
    }
    
    private void populateStrongholdData(RandomState noiseConfig) {
        LOGGER.info("Generating safe stronghold data. This may take up to a minute.");
        Set<Holder<Biome>> set = this.biomeSource.possibleBiomes();
        possibleStructureSets().map(Holder::value).forEach((holder) -> {
            boolean match = false;
            for(StructureSelectionEntry structureset_a : holder.structures()) {
                Structure structure = structureset_a.structure().value();
                Stream<Holder<Biome>> stream = structure.biomes().stream();
                if(stream.anyMatch(set::contains)) {
                    match = true;
                }
            }
            
            if(match) {
                if(holder.placement() instanceof ConcentricRingsStructurePlacement concentricringsstructureplacement) {
                    this.ringPositions.put(concentricringsstructureplacement, Lazy.lazy(
                            () -> this.generateRingPositions(holder, noiseConfig, concentricringsstructureplacement)));
                }
            }
        });
    }

    private List<ChunkPos> generateRingPositions(StructureSet holder, RandomState randomstate,
                                                 ConcentricRingsStructurePlacement concentricringsstructureplacement) { // Spigot
        if(concentricringsstructureplacement.count() == 0) {
            return List.of();
        }
        
        List<ChunkPos> list = new ArrayList<>();
        int i = concentricringsstructureplacement.distance();
        int j = concentricringsstructureplacement.count();
        int k = concentricringsstructureplacement.spread();
        HolderSet<Biome> holderset = concentricringsstructureplacement.preferredBiomes();
        RandomSource randomsource = RandomSource.create();
        
        if(this.conf.strongholdSeed != null && this.structureSets.getResourceKey(holder).orElse(null) ==
                                               net.minecraft.world.level.levelgen.structure.BuiltinStructureSets.STRONGHOLDS) {
            randomsource.setSeed(this.conf.strongholdSeed);
        } else {
            randomsource.setSeed(randomstate.legacyLevelSeed());
        }
        double d0 = randomsource.nextDouble() * 3.141592653589793D * 2.0D;
        int l = 0;
        int i1 = 0;
        
        for(int j1 = 0; j1 < j; ++j1) {
            double d1 = (double) (4 * i + i * i1 * 6) + (randomsource.nextDouble() - 0.5D) * (double) i * 2.5D;
            int k1 = (int) Math.round(Math.cos(d0) * d1);
            int l1 = (int) Math.round(Math.sin(d0) * d1);
            int i2 = SectionPos.sectionToBlockCoord(k1, 8);
            int j2 = SectionPos.sectionToBlockCoord(l1, 8);
            
            Objects.requireNonNull(holderset);
            Pair<BlockPos, Holder<Biome>> pair = this.biomeSource.findBiomeHorizontal(i2, 0, j2, 112, holderset::contains, randomsource,
                                                                                      randomstate.sampler());
            
            if(pair != null) {
                BlockPos blockposition = pair.getFirst();
                
                k1 = SectionPos.blockToSectionCoord(blockposition.getX());
                l1 = SectionPos.blockToSectionCoord(blockposition.getZ());
            }
            
            list.add(new ChunkPos(k1, l1));
            d0 += 6.283185307179586D / (double) k;
            ++l;
            if(l == k) {
                ++i1;
                l = 0;
                k += 2 * k / (i1 + 1);
                k = Math.min(k, j - j1);
                d0 += randomsource.nextDouble() * 3.141592653589793D * 2.0D;
            }
        }
        
        return list;
    }
}
