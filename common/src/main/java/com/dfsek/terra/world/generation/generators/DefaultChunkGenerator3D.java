package com.dfsek.terra.world.generation.generators;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;
import com.dfsek.terra.api.platform.block.data.Waterlogged;
import com.dfsek.terra.api.platform.world.BiomeGrid;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.api.util.world.PaletteUtil;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.generation.TerraBlockPopulator;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.palette.SinglePalette;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.profiler.ProfileFrame;
import com.dfsek.terra.world.Carver;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.carving.NoiseCarver;
import com.dfsek.terra.world.generation.math.samplers.Sampler;
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
    private final SinglePalette blank;
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

        carver = new NoiseCarver(new Range(0, 255), main.getWorldHandle().createBlockData("minecraft:air"), main);
        water = main.getWorldHandle().createBlockData("minecraft:water").getBlockType();
        blank = new SinglePalette(main.getWorldHandle().createBlockData("minecraft:air"));
    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return configPack.getTemplate().vanillaCaves();
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return configPack.getTemplate().vanillaDecorations();
    }

    @Override
    public boolean shouldGenerateMobs() {
        return configPack.getTemplate().vanillaMobs();
    }

    @Override
    public boolean shouldGenerateStructures() {
        return configPack.getTemplate().vanillaStructures();
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
        TerraWorld tw = main.getWorld(world);
        BiomeProvider grid = tw.getBiomeProvider();
        try(ProfileFrame ignore = main.getProfiler().profile("chunk_base_3d")) {
            if(!tw.isSafe()) return chunk;
            int xOrig = (chunkX << 4);
            int zOrig = (chunkZ << 4);

            Sampler sampler = tw.getConfig().getSamplerCache().getChunk(chunkX, chunkZ);

            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    int paletteLevel = 0;

                    int cx = xOrig + x;
                    int cz = zOrig + z;

                    TerraBiome b = grid.getBiome(xOrig + x, zOrig + z);
                    BiomeTemplate c = ((UserDefinedBiome) b).getConfig();

                    int sea = c.getSeaLevel();
                    Palette seaPalette = c.getOceanPalette();

                    boolean justSet = false;
                    BlockData data = null;
                    for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                        if(sampler.sample(x, y, z) > 0) {
                            justSet = true;
                            data = PaletteUtil.getPalette(x, y, z, c, sampler).get(paletteLevel, cx, y, cz);
                            chunk.setBlock(x, y, z, data);
                            if(paletteLevel == 0 && c.doSlabs() && y < 255) {
                                prepareBlockPartFloor(data, chunk.getBlockData(x, y + 1, z), chunk, new Vector3(x, y + 1, z), c.getSlabPalettes(),
                                        c.getStairPalettes(), c.getSlabThreshold(), sampler);
                            }
                            paletteLevel++;
                        } else if(y <= sea) {
                            chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, y, z + zOrig));
                            if(justSet && c.doSlabs()) {
                                prepareBlockPartCeiling(data, chunk.getBlockData(x, y, z), chunk, new Vector3(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
                            }
                            justSet = false;
                            paletteLevel = 0;
                        } else {
                            if(justSet && c.doSlabs()) {
                                prepareBlockPartCeiling(data, chunk.getBlockData(x, y, z), chunk, new Vector3(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
                            }
                            justSet = false;
                            paletteLevel = 0;
                        }
                    }
                }
            }
            if(configPack.getTemplate().doBetaCarvers()) {
                carver.carve(world, chunkX, chunkZ, chunk);
            }
            return chunk;
        }
    }

    private void prepareBlockPartFloor(BlockData down, BlockData orig, ChunkData chunk, Vector3 block, Map<BlockType, Palette> slabs,
                                       Map<BlockType, Palette> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getX(), block.getY() - 0.4, block.getZ()) > thresh) {
            if(stairs != null) {
                Palette stairPalette = stairs.get(down.getBlockType());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getX(), block.getY(), block.getZ()).clone();
                    if(stair instanceof Stairs) {
                        Stairs stairNew = (Stairs) stair;
                        if(placeStair(orig, chunk, block, thresh, sampler, stairNew)) return; // Successfully placed part.
                    }
                }
            }
            BlockData slab = slabs.getOrDefault(down.getBlockType(), blank).get(0, block.getX(), block.getY(), block.getZ());
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.getBlockType().equals(water));
            } else if(orig.getBlockType().equals(water)) return;
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private void prepareBlockPartCeiling(BlockData up, BlockData orig, ChunkData chunk, Vector3 block, Map<BlockType, Palette> slabs,
                                         Map<BlockType, Palette> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getX(), block.getY() + 0.4, block.getZ()) > thresh) {
            if(stairs != null) {
                Palette stairPalette = stairs.get(up.getBlockType());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getX(), block.getY(), block.getZ()).clone();
                    if(stair instanceof Stairs) {
                        Stairs stairNew = (Stairs) stair.clone();
                        stairNew.setHalf(Bisected.Half.TOP);
                        if(placeStair(orig, chunk, block, thresh, sampler, stairNew)) return; // Successfully placed part.
                    }
                }
            }
            BlockData slab = slabs.getOrDefault(up.getBlockType(), blank).get(0, block.getX(), block.getY(), block.getZ()).clone();
            if(slab instanceof Bisected) ((Bisected) slab).setHalf(Bisected.Half.TOP);
            if(slab instanceof Slab) ((Slab) slab).setType(Slab.Type.TOP);
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.getBlockType().equals(water));
            } else if(orig.getBlockType().equals(water)) return; // Only replace water if waterlogged.
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private boolean placeStair(BlockData orig, ChunkData chunk, Vector3 block, double thresh, Sampler sampler, Stairs stairNew) {

        if(sampler.sample(block.getBlockX() - 0.55, block.getY(), block.getZ()) > thresh) {

            stairNew.setFacing(BlockFace.WEST);
        } else if(sampler.sample(block.getBlockX(), block.getY(), block.getZ() - 0.55) > thresh) {
            stairNew.setFacing(BlockFace.NORTH);
        } else if(sampler.sample(block.getBlockX(), block.getY(), block.getZ() + 0.55) > thresh) {
            stairNew.setFacing(BlockFace.SOUTH);
        } else if(sampler.sample(block.getX() + 0.55, block.getY(), block.getZ()) > thresh) {
            stairNew.setFacing(BlockFace.EAST);
        } else stairNew = null;
        if(stairNew != null) {
            if(orig.getBlockType().equals(water)) stairNew.setWaterlogged(orig.getBlockType().equals(water));
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), stairNew);
            return true;
        }
        return false;
    }

    static void biomes(@NotNull World world, int chunkX, int chunkZ, @NotNull BiomeGrid biome, TerraPlugin main) {
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
