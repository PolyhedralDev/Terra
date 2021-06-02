package com.dfsek.terra.sponge.intern;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.api.world.locate.AsyncStructureFinder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.sponge.TerraSpongePlugin;
import com.dfsek.terra.sponge.intern.util.SpongeAdapter;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import com.dfsek.terra.world.generation.math.samplers.Sampler;
import com.dfsek.terra.world.population.items.TerraStructure;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jafama.FastMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.world.biome.provider.BiomeProvider;
import org.spongepowered.api.world.generation.config.structure.StructureGenerationConfig;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SpongeChunkGeneratorWrapper extends ChunkGenerator implements GeneratorWrapper {
    private final long seed;
    private final DefaultChunkGenerator3D delegate;
    private final TerraBiomeSource biomeSource;
    public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder.create(config -> config.group(
            Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID())
    ).apply(config, config.stable(TerraSpongePlugin.getInstance().getConfigRegistry()::get))));
    public static final Codec<SpongeChunkGeneratorWrapper> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TerraBiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
            PACK_CODEC.fieldOf("pack").stable().forGetter(generator -> generator.pack))
            .apply(instance, instance.stable(SpongeChunkGeneratorWrapper::new)));
    private final ConfigPack pack;

    public ConfigPack getPack() {
        return pack;
    }

    private DimensionType dimensionType;

    public SpongeChunkGeneratorWrapper(TerraBiomeSource biomeSource, long seed, ConfigPack configPack) {
        super(biomeSource, new StructureSettings(configPack.getTemplate().vanillaStructures()));
        this.pack = configPack;

        this.delegate = new DefaultChunkGenerator3D(pack, TerraSpongePlugin.getInstance());
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
        return new SpongeChunkGeneratorWrapper((TerraBiomeSource) this.biomeSource.withSeed(seed), seed, pack);
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, ChunkAccess chunkAccess) {

    }

    @Nullable
    @Override
    public BlockPos findNearestMapFeature(ServerLevel world, StructureFeature<?> feature, BlockPos center, int param3, boolean param4) {
        if(!pack.getTemplate().disableStructures()) {
            String name = Objects.requireNonNull(Registry.STRUCTURE_FEATURE.getKey(feature)).toString();
            TerraWorld terraWorld = TerraSpongePlugin.getInstance().getWorld((World) world);
            TerraStructure located = pack.getStructure(pack.getTemplate().getLocatable().get(name));
            if(located != null) {
                CompletableFuture<BlockPos> result = new CompletableFuture<>();
                AsyncStructureFinder finder = new AsyncStructureFinder(terraWorld.getBiomeProvider(), located, SpongeAdapter.adapt(center).toLocation((World) world), 0, 500, location -> {
                    result.complete(SpongeAdapter.adapt(location));
                }, TerraSpongePlugin.getInstance());
                finder.run(); // Do this synchronously.
                try {
                    return result.get();
                } catch(InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.findNearestMapFeature(world, feature, center, param3, param4);
    }


    @Override
    public boolean hasStronghold(@NotNull ChunkPos p_235952_1_) {
        if(pack.getTemplate().vanillaStructures()) return super.hasStronghold(p_235952_1_);
        return false;
    }

    @Override
    public void createStructures(RegistryAccess param0, StructureFeatureManager param1, ChunkAccess param2, StructureManager param3, long param4) {
        if(pack.getTemplate().vanillaStructures()) super.createStructures(param0, param1, param2, param3, param4);
    }

    @Override
    public void applyCarvers(long param0, BiomeManager param1, ChunkAccess param2, GenerationStep.Carving param3) {
        if(pack.getTemplate().vanillaCaves()) super.applyCarvers(param0, param1, param2, param3);
    }

    @Override
    public void fillFromNoise(@NotNull LevelAccessor world, @NotNull StructureFeatureManager p_230352_2_, @NotNull ChunkAccess chunk) {
        delegate.generateChunkData((World) world, new FastRandom(), chunk.getPos().x, chunk.getPos().z, (ChunkData) chunk);
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.@NotNull Types p_222529_3_) {
        TerraWorld world = TerraSpongePlugin.getInstance().getWorld(dimensionType);
        Sampler sampler = world.getConfig().getSamplerCache().getChunk(FastMath.floorDiv(x, 16), FastMath.floorDiv(z, 16));
        int cx = FastMath.floorMod(x, 16);
        int cz = FastMath.floorMod(z, 16);

        int height = world.getWorld().getMaxHeight();

        while(height >= 0 && sampler.sample(cx, height - 1, cz) < 0) height--;

        return height;
    }

    @Override
    public @NotNull BlockGetter getBaseColumn(int x, int z) {
        TerraWorld world = TerraSpongePlugin.getInstance().getWorld(dimensionType);
        int height = getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE);
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

        return new NoiseColumn(array);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        if(pack.getTemplate().vanillaMobs()) {
            int cx = region.getCenterX();
            int cy = region.getCenterZ();
            Biome biome = region.getBiome((new ChunkPos(cx, cy)).getWorldPosition());
            WorldgenRandom chunkRandom = new WorldgenRandom();
            chunkRandom.setDecorationSeed(region.getSeed(), cx << 4, cy << 4);
            NaturalSpawner.spawnMobsForChunkGeneration(region, biome, cx, cy, chunkRandom);
        }
    }


    @Override
    public TerraChunkGenerator getHandle() {
        return delegate;
    }

    public void setDimensionType(DimensionType dimensionType) {
        this.dimensionType = dimensionType;
    }
}