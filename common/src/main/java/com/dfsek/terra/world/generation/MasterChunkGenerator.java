package com.dfsek.terra.world.generation;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;
import com.dfsek.terra.api.platform.block.data.Waterlogged;
import com.dfsek.terra.api.platform.world.BiomeGrid;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.palette.SinglePalette;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.profiler.ProfileFuture;
import com.dfsek.terra.util.PaletteUtil;
import com.dfsek.terra.world.Carver;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.carving.NoiseCarver;
import com.dfsek.terra.world.generation.math.Sampler;
import com.dfsek.terra.world.generation.math.SamplerCache;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

public class MasterChunkGenerator implements TerraChunkGenerator {


    private final ConfigPack configPack;
    private final TerraPlugin main;
    private final MaterialData water;
    private final SinglePalette<BlockData> blank;

    private final Carver carver;

    private final SamplerCache cache;


    public MasterChunkGenerator(ConfigPack c, TerraPlugin main, SamplerCache cache) {
        this.configPack = c;
        this.main = main;
        carver = new NoiseCarver(new Range(0, 255), main.getWorldHandle().createBlockData("minecraft:air"), main);
        water = main.getWorldHandle().createMaterialData("minecraft:water");
        blank = new SinglePalette<>(main.getWorldHandle().createBlockData("minecraft:air"));
        this.cache = cache;
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
    public ChunkGenerator.ChunkData generateChunkData(@NotNull World world, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunk) {
        TerraWorld tw = main.getWorld(world);
        BiomeProvider grid = tw.getBiomeProvider();
        try(ProfileFuture ignore = tw.getProfiler().measure("TotalChunkGenTime")) {
            if(!tw.isSafe()) return chunk;
            int xOrig = (chunkX << 4);
            int zOrig = (chunkZ << 4);

            Sampler sampler = cache.getChunk(world, chunkX, chunkZ);

            for(byte x = 0; x < 16; x++) {
                for(byte z = 0; z < 16; z++) {
                    int paletteLevel = 0;

                    int cx = xOrig + x;
                    int cz = zOrig + z;

                    TerraBiome b = grid.getBiome(xOrig + x, zOrig + z);
                    BiomeTemplate c = ((UserDefinedBiome) b).getConfig();

                    int sea = c.getSeaLevel();
                    Palette<BlockData> seaPalette = c.getOceanPalette();

                    boolean justSet = false;
                    BlockData data = null;
                    for(int y = world.getMaxHeight() - 1; y >= 0; y--) {
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
            carver.carve(world, chunkX, chunkZ, chunk);
            return chunk;
        }
    }

    private void prepareBlockPartFloor(BlockData down, BlockData orig, ChunkGenerator.ChunkData chunk, Vector3 block, Map<MaterialData, Palette<BlockData>> slabs,
                                       Map<MaterialData, Palette<BlockData>> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getX(), block.getY() - 0.4, block.getZ()) > thresh) {
            if(stairs != null) {
                Palette<BlockData> stairPalette = stairs.get(down.getMaterial());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getX(), block.getY(), block.getZ()).clone();
                    if(stair instanceof Stairs) {
                        Stairs stairNew = (Stairs) stair;
                        if(placeStair(orig, chunk, block, thresh, sampler, stairNew)) return; // Successfully placed part.
                    }
                }
            }
            BlockData slab = slabs.getOrDefault(down.getMaterial(), blank).get(0, block.getX(), block.getY(), block.getZ());
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.matches(water));
            } else if(orig.matches(water)) return;
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private void prepareBlockPartCeiling(BlockData up, BlockData orig, ChunkGenerator.ChunkData chunk, Vector3 block, Map<MaterialData, Palette<BlockData>> slabs,
                                         Map<MaterialData, Palette<BlockData>> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getX(), block.getY() + 0.4, block.getZ()) > thresh) {
            if(stairs != null) {
                Palette<BlockData> stairPalette = stairs.get(up.getMaterial());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getX(), block.getY(), block.getZ()).clone();
                    if(stair instanceof Stairs) {
                        Stairs stairNew = (Stairs) stair.clone();
                        stairNew.setHalf(Bisected.Half.TOP);
                        if(placeStair(orig, chunk, block, thresh, sampler, stairNew)) return; // Successfully placed part.
                    }
                }
            }
            BlockData slab = slabs.getOrDefault(up.getMaterial(), blank).get(0, block.getX(), block.getY(), block.getZ()).clone();
            if(slab instanceof Bisected) ((Bisected) slab).setHalf(Bisected.Half.TOP);
            if(slab instanceof Slab) ((Slab) slab).setType(Slab.Type.TOP);
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.matches(water));
            } else if(orig.matches(water)) return; // Only replace water if waterlogged.
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private boolean placeStair(BlockData orig, ChunkGenerator.ChunkData chunk, Vector3 block, double thresh, Sampler sampler, Stairs stairNew) {

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
            if(orig.matches(water)) stairNew.setWaterlogged(orig.matches(water));
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), stairNew);
            return true;
        }
        return false;
    }

    @Override
    public void generateBiomes(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        int xOrig = (chunkX << 4);
        int zOrig = (chunkZ << 4);
        BiomeProvider grid = main.getWorld(world).getBiomeProvider();
        for(int x = 0; x < 4; x++) {
            for(byte z = 0; z < 4; z++) {
                int cx = xOrig + (x << 2);
                int cz = zOrig + (z << 2);
                TerraBiome b = grid.getBiome(cx, cz);

                biome.setBiome(x << 2, z << 2, b.getVanillaBiomes().get(b.getGenerator(world).getBiomeNoise(), cx, 0, cz));
            }
        }
    }

    public SamplerCache getCache() {
        return cache;
    }
}
