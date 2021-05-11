package com.dfsek.terra.forge.world.generator;

import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.forge.world.TerraBiomeSource;
import com.dfsek.terra.forge.world.handles.world.ForgeSeededWorldAccess;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import com.dfsek.terra.world.generation.math.samplers.Sampler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jafama.FastMath;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSpreadSettings;
import org.jetbrains.annotations.NotNull;

public class ForgeChunkGeneratorWrapper extends ChunkGenerator implements GeneratorWrapper {
    private final long seed;
    private final DefaultChunkGenerator3D delegate;
    private final TerraBiomeSource biomeSource;
    public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder.create(config -> config.group(
            Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID())
    ).apply(config, config.stable(TerraForgePlugin.getInstance().getConfigRegistry()::get))));
    public static final Codec<ForgeChunkGeneratorWrapper> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TerraBiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
            PACK_CODEC.fieldOf("pack").stable().forGetter(generator -> generator.pack))
            .apply(instance, instance.stable(ForgeChunkGeneratorWrapper::new)));
    private final ConfigPack pack;

    public ConfigPack getPack() {
        return pack;
    }

    public ForgeChunkGeneratorWrapper(TerraBiomeSource biomeSource, long seed, ConfigPack configPack) {
        super(biomeSource, new DimensionStructuresSettings(false));
        this.pack = configPack;

        this.delegate = new DefaultChunkGenerator3D(pack, TerraForgePlugin.getInstance());
        delegate.getMain().logger().info("Loading world with config pack " + pack.getTemplate().getID());
        this.biomeSource = biomeSource;

        this.seed = seed;
    }

    @Override
    protected @NotNull Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public @NotNull ChunkGenerator withSeed(long seed) {
        return new ForgeChunkGeneratorWrapper((TerraBiomeSource) this.biomeSource.withSeed(seed), seed, pack);
    }

    @Override
    public void buildSurfaceAndBedrock(@NotNull WorldGenRegion p_225551_1_, @NotNull IChunk p_225551_2_) {

    }

    @Override
    public boolean hasStronghold(@NotNull ChunkPos p_235952_1_) {
        return false;
    }

    @Override
    public void createStructures(@NotNull DynamicRegistries p_242707_1_, @NotNull StructureManager p_242707_2_, @NotNull IChunk p_242707_3_, @NotNull TemplateManager p_242707_4_, long p_242707_5_) {

    }

    @Override
    public void applyCarvers(long p_230350_1_, @NotNull BiomeManager p_230350_3_, @NotNull IChunk p_230350_4_, GenerationStage.@NotNull Carving p_230350_5_) {
        // No caves
    }

    @Override
    public void fillFromNoise(@NotNull IWorld world, @NotNull StructureManager p_230352_2_, @NotNull IChunk chunk) {
        ForgeSeededWorldAccess worldAccess = new ForgeSeededWorldAccess(world, seed, this);
        delegate.generateChunkData(worldAccess, new FastRandom(), chunk.getPos().x, chunk.getPos().z, new ForgeChunkData(chunk));
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.@NotNull Type p_222529_3_) {
        TerraWorld world = TerraForgePlugin.getInstance().getWorld(seed);
        Sampler sampler = world.getConfig().getSamplerCache().getChunk(FastMath.floorDiv(x, 16), FastMath.floorDiv(z, 16));
        int cx = FastMath.floorMod(x, 16);
        int cz = FastMath.floorMod(z, 16);

        int height = world.getWorld().getMaxHeight();

        while (height >= 0 && sampler.sample(cx, height - 1, cz) < 0) {
            height--;
        }

        return height;
    }

    @Override
    public @NotNull IBlockReader getBaseColumn(int p_230348_1_, int p_230348_2_) {
        int height = 64; // TODO: implementation
        BlockState[] array = new BlockState[256];
        for(int y = 255; y >= 0; y--) {
            if(y > height) {
                if(y > getSeaLevel()) {
                    array[y] = Blocks.AIR.defaultBlockState();
                } else {
                    array[y] = Blocks.WATER.defaultBlockState();
                }
            } else {
                array[y] = Blocks.STONE.defaultBlockState();
            }
        }

        return new Blockreader(array);
    }


    @Override
    public TerraChunkGenerator getHandle() {
        return delegate;
    }
}
