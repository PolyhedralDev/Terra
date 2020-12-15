package com.dfsek.terra.fabric.world.generator;

import com.dfsek.terra.api.gaea.math.MathUtil;
import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.world.TerraBiomeSource;
import com.dfsek.terra.fabric.world.handles.chunk.FabricChunkRegionChunk;
import com.dfsek.terra.fabric.world.handles.world.FabricSeededWorldAccess;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldChunkRegion;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.TreePopulator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class FabricChunkGeneratorWrapper extends ChunkGenerator implements Handle {
    private final long seed;
    private final TerraChunkGenerator delegate;
    private final TerraBiomeSource biomeSource;
    public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder.create(config -> config.group(
            Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID())
    ).apply(config, config.stable(TerraFabricPlugin.getInstance().getRegistry()::get))));
    public static final Codec<FabricChunkGeneratorWrapper> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TerraBiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
            PACK_CODEC.fieldOf("pack").stable().forGetter(generator -> generator.pack))
            .apply(instance, instance.stable(FabricChunkGeneratorWrapper::new)));
    private final ConfigPack pack;


    private final CavePopulator cavePopulator = new CavePopulator(TerraFabricPlugin.getInstance());
    private final FloraPopulator floraPopulator = new FloraPopulator(TerraFabricPlugin.getInstance());
    private final OrePopulator orePopulator = new OrePopulator(TerraFabricPlugin.getInstance());
    private final TreePopulator treePopulator = new TreePopulator(TerraFabricPlugin.getInstance());

    public FabricChunkGeneratorWrapper(TerraBiomeSource biomeSource, long seed, ConfigPack configPack) {
        super(biomeSource, new StructuresConfig(false));
        this.pack = configPack;

        this.delegate = new TerraChunkGenerator(configPack, TerraFabricPlugin.getInstance());
        delegate.getMain().getLogger().info("Loading world...");
        this.biomeSource = biomeSource;

        this.seed = seed;
    }

    @Override
    public TerraChunkGenerator getHandle() {
        return delegate;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new FabricChunkGeneratorWrapper((TerraBiomeSource) this.biomeSource.withSeed(seed), seed, pack);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {

    }

    @Override
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
        FabricSeededWorldAccess worldAccess = new FabricSeededWorldAccess(world, seed, this);
        delegate.generateChunkData(worldAccess, new FastRandom(), chunk.getPos().x, chunk.getPos().z, new FabricChunkData(chunk));
    }

    @Override
    public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver) {
        // No caves
    }

    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        FastRandom pop = new FastRandom(MathUtil.getCarverChunkSeed(region.getCenterChunkX(), region.getCenterChunkZ(), seed));
        FabricWorldChunkRegion chunkRegion = new FabricWorldChunkRegion(region, this);
        FabricChunkRegionChunk regionChunk = new FabricChunkRegionChunk(region);
        cavePopulator.populate(chunkRegion, pop, regionChunk);
        orePopulator.populate(chunkRegion, pop, regionChunk);
        floraPopulator.populate(chunkRegion, pop, regionChunk);
    }

    @Override
    public void setStructureStarts(DynamicRegistryManager dynamicRegistryManager, StructureAccessor structureAccessor, Chunk chunk, StructureManager structureManager, long worldSeed) {

    }

    @Override
    public boolean isStrongholdStartingChunk(ChunkPos chunkPos) {
        return false;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return 0;
    }

    @Override
    public BlockView getColumnSample(int x, int z) {
        int height = 64; // TODO: implementation
        BlockState[] array = new BlockState[256];
        for(int y = 255; y >= 0; y--) {
            if(y > height) {
                if(y > getSeaLevel()) {
                    array[y] = Blocks.AIR.getDefaultState();
                } else {
                    array[y] = Blocks.WATER.getDefaultState();
                }
            } else {
                array[y] = Blocks.STONE.getDefaultState();
            }
        }

        return new VerticalBlockSample(array);
    }
}
