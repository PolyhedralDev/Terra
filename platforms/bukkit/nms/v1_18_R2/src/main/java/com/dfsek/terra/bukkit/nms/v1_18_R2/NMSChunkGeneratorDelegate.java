package com.dfsek.terra.bukkit.nms.v1_18_R2;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class NMSChunkGeneratorDelegate extends ChunkGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSChunkGeneratorDelegate.class);
    private static final Lazy<List<ChunkPos>> EMPTY = Lazy.lazy(List::of);
    private final NMSBiomeProvider biomeSource;
    private final com.dfsek.terra.api.world.chunk.generation.ChunkGenerator delegate;
    private final ChunkGenerator vanilla;
    private final ConfigPack pack;
    private final long seed;
    private final Map<ConcentricRingsStructurePlacement, Lazy<List<ChunkPos>>> ringPositions = new Object2ObjectArrayMap<>();
    private volatile boolean rings = false;
    
    public NMSChunkGeneratorDelegate(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, long seed) {
        super(Registries.structureSet(), Optional.empty(), biomeProvider, biomeProvider, seed);
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        this.vanilla = vanilla;
        this.biomeSource = biomeProvider;
        this.pack = pack;
        this.seed = seed;
    }
    
    @Override
    public void applyCarvers(@NotNull WorldGenRegion chunkRegion, long seed, @NotNull BiomeManager biomeAccess,
                             @NotNull StructureFeatureManager structureAccessor,
                             @NotNull ChunkAccess chunk, GenerationStep.@NotNull Carving generationStep) {
        // no-op
    }
    
    @Override
    public void applyBiomeDecoration(@NotNull WorldGenLevel world, @NotNull ChunkAccess chunk,
                                     @NotNull StructureFeatureManager structureAccessor) {
        vanilla.applyBiomeDecoration(world, chunk, structureAccessor);
    }
    
    @Override
    public int getSeaLevel() {
        return vanilla.getSeaLevel();
    }
    
    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Executor executor, @NotNull Blender blender,
                                                                 @NotNull StructureFeatureManager structureAccessor,
                                                                 @NotNull ChunkAccess chunk) {
        return vanilla.fillFromNoise(executor, blender, structureAccessor, chunk);
    }
    
    @Override
    public void buildSurface(@NotNull WorldGenRegion region, @NotNull StructureFeatureManager structures, @NotNull ChunkAccess chunk) {
        // no-op
    }
    
    @Override
    protected @NotNull Codec<? extends ChunkGenerator> codec() {
        return ChunkGenerator.CODEC;
    }
    
    @Override
    public @NotNull NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height) {
        /*
        BlockState[] array = new BlockState[height.getHeight()];
        WorldProperties properties = new NMSWorldProperties(seed, height);
        BiomeProvider biomeProvider = pack.getBiomeProvider().caching(properties);
        for(int y = properties.getMaxHeight() - 1; y >= properties.getMinHeight(); y--) {
            array[y - properties.getMinHeight()] = ((CraftBlockData) delegate.getBlock(properties, x, y, z, biomeProvider)
                                                                             .getHandle()).getState();
        }
        return new NoiseColumn(getMinY(), array);
         */
        return vanilla.getBaseColumn(x, z, height);
    }
    
    @Override // withSeed
    public @NotNull ChunkGenerator withSeed(long seed) {
        return new NMSChunkGeneratorDelegate(vanilla, pack, biomeSource, seed);
    }
    
    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion regionlimitedworldaccess) {
        vanilla.spawnOriginalMobs(regionlimitedworldaccess);
    }
    
    @Override
    public int getGenDepth() {
        return vanilla.getGenDepth();
    }
    
    @Override
    public @NotNull Sampler climateSampler() {
        return Climate.empty();
    }
    
    @Override
    public int getMinY() {
        return vanilla.getMinY();
    }
    
    @Override
    public int getBaseHeight(int x, int z, Heightmap.@NotNull Types heightmap, @NotNull LevelHeightAccessor world) {
        WorldProperties properties = new NMSWorldProperties(seed, world);
        int y = properties.getMaxHeight();
        BiomeProvider biomeProvider = pack.getBiomeProvider();
        while(y >= getMinY() && !heightmap.isOpaque().test(
                ((CraftBlockData) delegate.getBlock(properties, x, y - 1, z, biomeProvider).getHandle()).getState())) {
            y--;
        }
        return y;
    }
    
    @Nullable
    @Override
    public List<ChunkPos> getRingPositionsFor(@NotNull ConcentricRingsStructurePlacement concentricringsstructureplacement) {
        ensureStructuresGenerated();
        return ringPositions.getOrDefault(concentricringsstructureplacement, EMPTY).value();
    }
    
    @Override
    public synchronized void ensureStructuresGenerated() {
        if(!this.rings) {
            super.ensureStructuresGenerated();
            this.populateStrongholdData();
            this.rings = true;
        }
    }
    
    private void populateStrongholdData() {
        LOGGER.info("Generating safe stronghold data. This may take up to a minute.");
        Set<Holder<Biome>> set = this.runtimeBiomeSource.possibleBiomes();
        possibleStructureSets().map(Holder::value).forEach((holder) -> { // we dont need the spigot crap because it doesnt touch concentric.
            StructurePlacement structureplacement = holder.placement();
            if(structureplacement instanceof ConcentricRingsStructurePlacement concentricringsstructureplacement) {
                if(holder.structures().stream().anyMatch((structureset_a1) -> structureset_a1.generatesInMatchingBiome(set::contains))) {
                    this.ringPositions.put(concentricringsstructureplacement,
                                           Lazy.lazy(() -> this.generateRingPositions(holder, concentricringsstructureplacement)));
                }
            }
        });
    }
    
    private List<ChunkPos> generateRingPositions(StructureSet holder,
                                                 ConcentricRingsStructurePlacement concentricringsstructureplacement) { // Spigot
        if(concentricringsstructureplacement.count() == 0) {
            return List.of();
        }
        
        List<ChunkPos> list = new ArrayList<>();
        Set<Holder<Biome>> set = holder
                .structures()
                .stream()
                .flatMap((structureset_a) -> structureset_a.structure().value().biomes().stream())
                .collect(Collectors.toSet());
        int i = concentricringsstructureplacement.distance();
        int j = concentricringsstructureplacement.count();
        int k = concentricringsstructureplacement.spread();
        Random random = new Random();
        
        // Paper start
        if(this.conf.strongholdSeed != null && this.structureSets.getResourceKey(holder).orElse(null) ==
                                               net.minecraft.world.level.levelgen.structure.BuiltinStructureSets.STRONGHOLDS) {
            random.setSeed(this.conf.strongholdSeed);
        } else {
            // Paper end
            random.setSeed(this.ringPlacementSeed);
        } // Paper
        double d0 = random.nextDouble() * 3.141592653589793D * 2.0D;
        int l = 0;
        int i1 = 0;
        
        for(int j1 = 0; j1 < j; ++j1) {
            double d1 = (double) (4 * i + i * i1 * 6) + (random.nextDouble() - 0.5D) * (double) i * 2.5D;
            int k1 = (int) Math.round(Math.cos(d0) * d1);
            int l1 = (int) Math.round(Math.sin(d0) * d1);
            int i2 = SectionPos.sectionToBlockCoord(k1, 8);
            int j2 = SectionPos.sectionToBlockCoord(l1, 8);
            
            Objects.requireNonNull(set);
            Pair<BlockPos, Holder<Biome>> pair = this.biomeSource.findBiomeHorizontal(i2, 0, j2, 112, set::contains, random,
                                                                                      this.climateSampler());
            
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
                d0 += random.nextDouble() * 3.141592653589793D * 2.0D;
            }
        }
        
        return list;
    }
    
    @Override
    public void addDebugScreenInfo(@NotNull List<String> arg0, @NotNull BlockPos arg1) {
    
    }
}
