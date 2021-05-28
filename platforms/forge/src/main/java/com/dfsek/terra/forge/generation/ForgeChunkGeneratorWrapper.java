package com.dfsek.terra.forge.generation;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.api.world.locate.AsyncStructureFinder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.ForgeAdapter;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.forge.block.ForgeBlockData;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import com.dfsek.terra.world.generation.math.samplers.Sampler;
import com.dfsek.terra.world.population.items.TerraStructure;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jafama.FastMath;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Blockreader;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    private DimensionType dimensionType;

    public ForgeChunkGeneratorWrapper(TerraBiomeSource biomeSource, long seed, ConfigPack configPack) {
        super(biomeSource, new DimensionStructuresSettings(configPack.getTemplate().vanillaStructures()));
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

    @Nullable
    @Override
    public BlockPos findNearestMapFeature(@NotNull ServerWorld world, @NotNull Structure<?> feature, @NotNull BlockPos center, int radius, boolean skipExistingChunks) {
        if(!pack.getTemplate().disableStructures()) {
            String name = Objects.requireNonNull(Registry.STRUCTURE_FEATURE.getKey(feature)).toString();
            TerraWorld terraWorld = TerraForgePlugin.getInstance().getWorld((World) world);
            TerraStructure located = pack.getStructure(pack.getTemplate().getLocatable().get(name));
            if(located != null) {
                CompletableFuture<BlockPos> result = new CompletableFuture<>();
                AsyncStructureFinder finder = new AsyncStructureFinder(terraWorld.getBiomeProvider(), located, ForgeAdapter.adapt(center).toLocation((World) world), 0, 500, location -> {
                    result.complete(ForgeAdapter.adapt(location));
                }, TerraForgePlugin.getInstance());
                finder.run(); // Do this synchronously.
                try {
                    return result.get();
                } catch(InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.findNearestMapFeature(world, feature, center, radius, skipExistingChunks);
    }

    @Override
    public boolean hasStronghold(@NotNull ChunkPos p_235952_1_) {
        if(pack.getTemplate().vanillaStructures()) return super.hasStronghold(p_235952_1_);
        return false;
    }

    @Override
    public void createStructures(@NotNull DynamicRegistries dynamicRegistries, @NotNull StructureManager manager, @NotNull IChunk chunk, @NotNull TemplateManager templateManager, long p_242707_5_) {
        if(pack.getTemplate().vanillaStructures()) super.createStructures(dynamicRegistries, manager, chunk, templateManager, p_242707_5_);
    }

    @Override
    public void applyCarvers(long p_230350_1_, @NotNull BiomeManager biomeManager, @NotNull IChunk chunk, GenerationStage.@NotNull Carving carving) {
        if(pack.getTemplate().vanillaCaves()) super.applyCarvers(p_230350_1_, biomeManager, chunk, carving);
    }

    @Override
    public void fillFromNoise(@NotNull IWorld world, @NotNull StructureManager p_230352_2_, @NotNull IChunk chunk) {
        delegate.generateChunkData((World) world, new FastRandom(), chunk.getPos().x, chunk.getPos().z, (ChunkData) chunk);
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.@NotNull Type type) {
        TerraWorld world = TerraForgePlugin.getInstance().getWorld(dimensionType);
        int height = world.getWorld().getMaxHeight();
        while(height >= 0 && !type.isOpaque().test(((ForgeBlockData) world.getUngeneratedBlock(x, height-1, z)).getHandle())) {
            height--;
        }
        return height;
    }

    @Override
    public @NotNull IBlockReader getBaseColumn(int x, int z) {
        TerraWorld world = TerraForgePlugin.getInstance().getWorld(dimensionType);
        int height = getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE);
        BlockState[] array = new BlockState[256];
        for(int y = 255; y >= 0; y--) {
            if(y > height) {
                if(y > ((UserDefinedBiome) world.getBiomeProvider().getBiome(x, z)).getConfig().getSeaLevel()) {
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
    public void spawnOriginalMobs(WorldGenRegion region) {
        if(pack.getTemplate().vanillaMobs()) {
            int cx = region.getCenterX();
            int cy = region.getCenterZ();
            Biome biome = region.getBiome((new ChunkPos(cx, cy)).getWorldPosition());
            SharedSeedRandom chunkRandom = new SharedSeedRandom();
            chunkRandom.setDecorationSeed(region.getSeed(), cx << 4, cy << 4);
            WorldEntitySpawner.spawnMobsForChunkGeneration(region, biome, cx, cy, chunkRandom);
        }
    }

    @Override
    public List<MobSpawnInfo.Spawners> getMobsAt(Biome p_230353_1_, StructureManager p_230353_2_, EntityClassification p_230353_3_, BlockPos p_230353_4_) {
        List<MobSpawnInfo.Spawners> spawns = net.minecraftforge.common.world.StructureSpawnManager.getStructureSpawns(p_230353_2_, p_230353_3_, p_230353_4_);
        if(spawns != null) return spawns;
        return super.getMobsAt(p_230353_1_, p_230353_2_, p_230353_3_, p_230353_4_);
    }

    @Override
    public TerraChunkGenerator getHandle() {
        return delegate;
    }

    public void setDimensionType(DimensionType dimensionType) {
        this.dimensionType = dimensionType;
    }
}
