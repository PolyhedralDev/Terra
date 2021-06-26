package com.dfsek.terra.world.generation.generators;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.block.state.properties.enums.Direction;
import com.dfsek.terra.api.block.state.properties.enums.Half;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.math.range.ConstantRange;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.PaletteUtil;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.BiomeGrid;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkData;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.generator.Sampler;
import com.dfsek.terra.api.world.generator.TerraBlockPopulator;
import com.dfsek.terra.api.world.generator.TerraChunkGenerator;
import com.dfsek.terra.api.world.palette.PaletteImpl;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.vector.Vector3Impl;
import com.dfsek.terra.world.Carver;
import com.dfsek.terra.world.carving.NoiseCarver;
import com.dfsek.terra.world.generation.math.samplers.Sampler3D;
import com.dfsek.terra.world.population.CavePopulator;
import com.dfsek.terra.world.population.FloraPopulator;
import com.dfsek.terra.world.population.OrePopulator;
import com.dfsek.terra.world.population.StructurePopulator;
import com.dfsek.terra.world.population.TreePopulator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DefaultChunkGenerator3D implements TerraChunkGenerator {
    private final ConfigPack configPack;
    private final TerraPlugin main;
    private final BlockType water;
    private final PaletteImpl.Singleton blank;
    private final List<TerraBlockPopulator> blockPopulators = new ArrayList<>();

    private final Carver carver;

    public DefaultChunkGenerator3D(ConfigPack c, TerraPlugin main) {
        this.configPack = c;
        this.main = main;

        blockPopulators.add(new CavePopulator(main));
        blockPopulators.add(new StructurePopulator(main));
        blockPopulators.add(new OrePopulator(main));
        blockPopulators.add(new TreePopulator(main));
        blockPopulators.add(new FloraPopulator(main));

        carver = new NoiseCarver(new ConstantRange(0, 255), main.getWorldHandle().createBlockData("minecraft:air"), main);
        water = main.getWorldHandle().createBlockData("minecraft:water").getBlockType();
        blank = new PaletteImpl.Singleton(main.getWorldHandle().createBlockData("minecraft:air"));
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

            if(!tw.isSafe()) return chunk;
            int xOrig = (chunkX << 4);
            int zOrig = (chunkZ << 4);

            Sampler sampler = tw.getConfig().getSamplerCache().getChunk(chunkX, chunkZ);

            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    int paletteLevel = 0;

                    int cx = xOrig + x;
                    int cz = zOrig + z;

                    TerraBiome b = grid.getBiome(cx, cz);
                    BiomeTemplate c = ((UserDefinedBiome) b).getConfig();

                    int sea = c.getSeaLevel();
                    Palette seaPalette = c.getOceanPalette();

                    boolean justSet = false;
                    BlockState data = null;
                    for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                        if(sampler.sample(x, y, z) > 0) {
                            justSet = true;
                            data = PaletteUtil.getPalette(x, y, z, c, sampler).get(paletteLevel, cx, y, cz);
                            chunk.setBlock(x, y, z, data);
                            if(paletteLevel == 0 && c.doSlabs() && y < 255) {
                                prepareBlockPartFloor(data, chunk.getBlock(x, y + 1, z), chunk, new Vector3Impl(x, y + 1, z), c.getSlabPalettes(),
                                        c.getStairPalettes(), c.getSlabThreshold(), sampler);
                            }
                            paletteLevel++;
                        } else if(y <= sea) {
                            chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, y, z + zOrig));
                            if(justSet && c.doSlabs()) {
                                prepareBlockPartCeiling(data, chunk.getBlock(x, y, z), chunk, new Vector3Impl(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
                            }
                            justSet = false;
                            paletteLevel = 0;
                        } else {
                            if(justSet && c.doSlabs()) {
                                prepareBlockPartCeiling(data, chunk.getBlock(x, y, z), chunk, new Vector3Impl(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
                            }
                            justSet = false;
                            paletteLevel = 0;
                        }
                    }
                }
            }
            if(configPack.doBetaCarvers()) {
                carver.carve(world, chunkX, chunkZ, chunk);
            }
            return chunk;
        }
    }

    private void prepareBlockPartFloor(BlockState down, BlockState orig, ChunkData chunk, Vector3 block, Map<BlockType, Palette> slabs,
                                       Map<BlockType, Palette> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getX(), block.getY() - 0.4, block.getZ()) > thresh) {
            if(stairs != null) {
                Palette stairPalette = stairs.get(down.getBlockType());
                if(stairPalette != null) {
                    BlockState stair = stairPalette.get(0, block.getX(), block.getY(), block.getZ()).clone();
                    if(stair.has(Properties.DIRECTION)) {
                        BlockState stairNew = stair.clone();
                        if(placeStair(orig, chunk, block, thresh, sampler, stairNew)) return; // Successfully placed part.
                    }
                }
            }
            BlockState slab = slabs.getOrDefault(down.getBlockType(), blank).get(0, block.getX(), block.getY(), block.getZ())
                    .setIfPresent(Properties.WATERLOGGED, orig.getBlockType().equals(water));

            if(orig.getBlockType().equals(water)) return;
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private void prepareBlockPartCeiling(BlockState up, BlockState orig, ChunkData chunk, Vector3 block, Map<BlockType, Palette> slabs,
                                         Map<BlockType, Palette> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getX(), block.getY() + 0.4, block.getZ()) > thresh) {
            if(stairs != null) {
                Palette stairPalette = stairs.get(up.getBlockType());
                if(stairPalette != null) {
                    BlockState stair = stairPalette.get(0, block.getX(), block.getY(), block.getZ()).clone();
                    stair.setIfPresent(Properties.HALF, Half.TOP);
                    if(stair.has(Properties.DIRECTION)) {
                        BlockState stairNew = stair.clone();
                        if(placeStair(orig, chunk, block, thresh, sampler, stairNew)) return; // Successfully placed part.
                    }
                }
            }
            BlockState slab = slabs.getOrDefault(up.getBlockType(), blank).get(0, block.getX(), block.getY(), block.getZ()).clone();
            slab.setIfPresent(Properties.HALF, Half.TOP);

            slab.setIfPresent(Properties.WATERLOGGED, orig.getBlockType().isWater());
            if(orig.getBlockType().equals(water)) return;

            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
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
            stairNew.setIfPresent(Properties.WATERLOGGED, orig.getBlockType().equals(water));
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), stairNew);
            return true;
        }
        return false;
    }

    @SuppressWarnings({"try"})
    static void biomes(@NotNull World world, int chunkX, int chunkZ, @NotNull BiomeGrid biome, TerraPlugin main) {
        try(ProfileFrame ignore = main.getProfiler().profile("biomes")) {
            int xOrig = (chunkX << 4);
            int zOrig = (chunkZ << 4);
            BiomeProvider grid = main.getWorld(world).getBiomeProvider();
            for(int x = 0; x < 4; x++) {
                for(int z = 0; z < 4; z++) {
                    int cx = xOrig + (x << 2);
                    int cz = zOrig + (z << 2);
                    TerraBiome b = grid.getBiome(cx, cz);

                    biome.setBiome(cx, cz, b.getVanillaBiomes().get(b.getGenerator(world).getBiomeNoise(), cx, 0, cz));
                }
            }
        }
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
    public List<TerraBlockPopulator> getPopulators() {
        return blockPopulators;
    }
}
