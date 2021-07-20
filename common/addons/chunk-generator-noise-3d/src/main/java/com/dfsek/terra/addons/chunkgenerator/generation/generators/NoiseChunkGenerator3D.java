package com.dfsek.terra.addons.chunkgenerator.generation.generators;

import com.dfsek.terra.addons.chunkgenerator.PaletteUtil;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.Sampler3D;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteInfo;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.block.state.properties.enums.Direction;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.BiomeGrid;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkData;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.generator.Sampler;
import com.dfsek.terra.api.world.generator.TerraChunkGenerator;
import com.dfsek.terra.api.world.generator.TerraGenerationStage;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoiseChunkGenerator3D implements TerraChunkGenerator {
    private final ConfigPack configPack;
    private final TerraPlugin main;
    private final List<TerraGenerationStage> generationStages = new ArrayList<>();

    private final BlockState air;

    public NoiseChunkGenerator3D(ConfigPack c, TerraPlugin main) {
        this.configPack = c;
        this.main = main;
        this.air = main.getWorldHandle().air();
        c.getStages().forEach(stage -> generationStages.add(stage.newInstance(c)));
    }

    @SuppressWarnings({"try"})
    static void biomes(@NotNull World world, int chunkX, int chunkZ, @NotNull BiomeGrid biome, TerraPlugin main) {
        try(ProfileFrame ignore = main.getProfiler().profile("biomes")) {
            int xOrig = (chunkX << 4);
            int zOrig = (chunkZ << 4);
            long seed = world.getSeed();
            BiomeProvider grid = main.getWorld(world).getBiomeProvider();
            for(int x = 0; x < 4; x++) {
                for(int z = 0; z < 4; z++) {
                    int cx = xOrig + (x << 2);
                    int cz = zOrig + (z << 2);
                    TerraBiome b = grid.getBiome(cx, cz, seed);

                    biome.setBiome(cx, cz, b.getVanillaBiomes().get(b.getGenerator(world).getBiomeNoise(), cx, 0, cz, world.getSeed()));
                }
            }
        }
    }

    @Override
    public ConfigPack getConfigPack() {
        return configPack;
    }

    @Override
    public TerraPlugin getMain() {
        return main;
    }

    @Override
    @SuppressWarnings({"try"})
    public ChunkData generateChunkData(@NotNull World world, Random random, int chunkX, int chunkZ, ChunkData chunk) {
        try(ProfileFrame ignore = main.getProfiler().profile("chunk_base_3d")) {
            TerraWorld tw = main.getWorld(world);
            BiomeProvider grid = tw.getBiomeProvider();

            int xOrig = (chunkX << 4);
            int zOrig = (chunkZ << 4);

            Sampler sampler = tw.getConfig().getSamplerCache().getChunk(chunkX, chunkZ);

            long seed = world.getSeed();

            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    int paletteLevel = 0;

                    int cx = xOrig + x;
                    int cz = zOrig + z;

                    TerraBiome biome = grid.getBiome(cx, cz, seed);

                    PaletteInfo paletteInfo = biome.getContext().get(PaletteInfo.class);

                    if(paletteInfo == null) {
                        main.logger().info("null palette: " + biome.getID());
                    }

                    Generator generator = biome.getGenerator(world);

                    int sea = paletteInfo.getSeaLevel();
                    Palette seaPalette = paletteInfo.getOcean();

                    boolean justSet = false;
                    BlockState data = null;
                    for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                        if(sampler.sample(x, y, z) > 0) {
                            justSet = true;

                            data = PaletteUtil.getPalette(x, y, z, generator, sampler, paletteInfo).get(paletteLevel, cx, y, cz, seed);
                            chunk.setBlock(x, y, z, data);

                            paletteLevel++;
                        } else if(y <= sea) {
                            chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, y, z + zOrig, seed));

                            justSet = false;
                            paletteLevel = 0;
                        }  else {

                            justSet = false;
                            paletteLevel = 0;
                        }
                    }
                }
            }
            return chunk;
        }
    }

    private boolean placeStair(BlockState orig, ChunkData chunk, Vector3 block, double thresh, Sampler sampler, BlockState stairNew) {

        if(sampler.sample(block.getBlockX() - 0.55, block.getY(), block.getZ()) > thresh) {
            stairNew.set(Properties.DIRECTION, Direction.WEST);
        } else if(sampler.sample(block.getBlockX(), block.getY(), block.getZ() - 0.55) > thresh) {
            stairNew.set(Properties.DIRECTION, Direction.NORTH);
        } else if(sampler.sample(block.getBlockX(), block.getY(), block.getZ() + 0.55) > thresh) {
            stairNew.set(Properties.DIRECTION, Direction.SOUTH);
        } else if(sampler.sample(block.getX() + 0.55, block.getY(), block.getZ()) > thresh) {
            stairNew.set(Properties.DIRECTION, Direction.EAST);
        } else stairNew = null;
        if(stairNew != null) {
            stairNew.setIfPresent(Properties.WATERLOGGED, orig.getBlockType().isWater());
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), stairNew);
            return true;
        }
        return false;
    }

    @Override
    public void generateBiomes(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        biomes(world, chunkX, chunkZ, biome, main);
    }

    @Override
    public Sampler createSampler(int chunkX, int chunkZ, BiomeProvider provider, World world, int elevationSmooth) {
        return new Sampler3D(chunkX, chunkZ, provider, world, elevationSmooth);
    }

    @Override
    public List<TerraGenerationStage> getGenerationStages() {
        return generationStages;
    }

    @Override
    public BlockState getBlock(World world, int x, int y, int z) {
        TerraWorld terraWorld = main.getWorld(world);
        BiomeProvider provider = terraWorld.getBiomeProvider();
        TerraBiome biome = provider.getBiome(x, z, world.getSeed());
        Sampler sampler = terraWorld.getConfig().getSamplerCache().get(x, z);

        PaletteInfo paletteInfo = biome.getContext().get(PaletteInfo.class);
        Palette palette = PaletteUtil.getPalette(x, y, z, biome.getGenerator(world), sampler, paletteInfo);
        int fdX = FastMath.floorMod(x, 16);
        int fdZ = FastMath.floorMod(z, 16);
        double noise = sampler.sample(fdX, y, fdZ);
        if(noise > 0) {
            int level = 0;
            for(int yi = world.getMaxHeight() - 1; yi > y; yi--) {
                if(sampler.sample(fdX, yi, fdZ) > 0) level++;
                else level = 0;
            }
            return palette.get(level, x, y, z, world.getSeed());
        } else if(y <= paletteInfo.getSeaLevel()) {
            return paletteInfo.getOcean().get(paletteInfo.getSeaLevel() - y, x, y, z, world.getSeed());
        } else return air;
    }
}
