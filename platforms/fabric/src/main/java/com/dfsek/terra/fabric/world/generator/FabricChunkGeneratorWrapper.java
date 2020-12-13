package com.dfsek.terra.fabric.world.generator;

import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.world.FabricBiomeGrid;
import com.dfsek.terra.fabric.world.handles.FabricSeededWorldAccess;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class FabricChunkGeneratorWrapper extends ChunkGenerator implements Handle {
    private final long seed;
    private final TerraChunkGenerator delegate;
    private final Codec<FabricChunkGeneratorWrapper> codec = RecordCodecBuilder.create(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed))
            .apply(instance, instance.stable(FabricChunkGeneratorWrapper::new)));

    public FabricChunkGeneratorWrapper(BiomeSource biomeSource, long seed) {
        super(biomeSource, new StructuresConfig(false));
        this.delegate = new TerraChunkGenerator(TerraFabricPlugin.getInstance().getRegistry().get("DEFAULT"), TerraFabricPlugin.getInstance());
        delegate.getMain().getLogger().info("Loading world...");

        this.seed = seed;
    }

    @Override
    public TerraChunkGenerator getHandle() {
        return delegate;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return codec;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new FabricChunkGeneratorWrapper(this.biomeSource.withSeed(seed), seed);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {

    }

    @Override
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
        delegate.generateChunkData(new FabricSeededWorldAccess(world, seed, this), new FastRandom(), chunk.getPos().x, chunk.getPos().z, new FabricBiomeGrid(), new FabricChunkData(chunk));
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
