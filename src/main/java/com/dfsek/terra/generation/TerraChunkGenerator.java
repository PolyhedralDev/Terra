package com.dfsek.terra.generation;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.math.MathUtil;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.StructurePopulator;
import com.dfsek.terra.population.TreePopulator;
import com.dfsek.terra.util.DataUtil;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.population.PopulationManager;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.profiler.WorldProfiler;
import org.polydev.gaea.world.palette.Palette;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;


public class TerraChunkGenerator extends GaeaChunkGenerator {
    private static final Map<World, PopulationManager> popMap = new HashMap<>();
    private final PopulationManager popMan = new PopulationManager(Terra.getInstance());
    private final ConfigPack configPack;
    private boolean needsLoad = true;

    public TerraChunkGenerator(ConfigPack c) {
        super(ChunkInterpolator.InterpolationType.TRILINEAR);
        this.configPack = c;
        popMan.attach(new OrePopulator());
        popMan.attach(new TreePopulator());
        popMan.attach(new FloraPopulator());
    }

    public static synchronized void saveAll() {
        for(Map.Entry<World, PopulationManager> e : popMap.entrySet()) {
            try {
                e.getValue().saveBlocks(e.getKey());
                Debug.info("Saved data for world " + e.getKey().getName());
            } catch(IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static synchronized void fixChunk(Chunk c) {
        if(!(c.getWorld().getGenerator() instanceof TerraChunkGenerator)) throw new IllegalArgumentException();
        popMap.get(c.getWorld()).checkNeighbors(c.getX(), c.getZ(), c.getWorld());
    }

    private static Palette<BlockData> getPalette(int x, int y, int z, BiomeTemplate c, Sampler sampler) {
        PaletteHolder slant = c.getSlantPalette();
        if(slant != null && MathUtil.derivative(sampler, x, y, z) > c.getSlantThreshold()) {
            return slant.getPalette(y);
        }
        return c.getPalette().getPalette(y);
    }

    private static void prepareBlockPartFloor(BlockData down, BlockData orig, ChunkData chunk, Vector block, Map<Material, Palette<BlockData>> slabs,
                                              Map<Material, Palette<BlockData>> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getBlockX(), block.getBlockY() - 0.4, block.getBlockZ()) > thresh) {
            if(stairs != null) {
                Palette<BlockData> stairPalette = stairs.get(down.getMaterial());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getBlockX(), block.getBlockZ());
                    Stairs stairNew = (Stairs) stair.clone();
                    if(placePart(orig, chunk, block, thresh, sampler, stairNew)) return;
                }
            }
            BlockData slab = slabs.getOrDefault(down.getMaterial(), DataUtil.BLANK_PALETTE).get(0, block.getBlockX(), block.getBlockZ());
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.matches(DataUtil.WATER));
            } else if(orig.matches(DataUtil.WATER)) return;
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private static void prepareBlockPartCeiling(BlockData up, BlockData orig, ChunkData chunk, Vector block, Map<Material, Palette<BlockData>> slabs,
                                                Map<Material, Palette<BlockData>> stairs, double thresh, Sampler sampler) {
        if(sampler.sample(block.getBlockX(), block.getBlockY() + 0.4, block.getBlockZ()) > thresh) {
            if(stairs != null) {
                Palette<BlockData> stairPalette = stairs.get(up.getMaterial());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getBlockX(), block.getBlockZ()).clone();
                    Stairs stairNew = (Stairs) stair.clone();
                    stairNew.setHalf(Bisected.Half.TOP);
                    if(placePart(orig, chunk, block, thresh, sampler, stairNew)) return;
                }
            }
            BlockData slab = slabs.getOrDefault(up.getMaterial(), DataUtil.BLANK_PALETTE).get(0, block.getBlockX(), block.getBlockZ()).clone();
            if(slab instanceof Bisected) ((Bisected) slab).setHalf(Bisected.Half.TOP);
            if(slab instanceof Slab) ((Slab) slab).setType(Slab.Type.TOP);
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.matches(DataUtil.WATER));
            } else if(orig.matches(DataUtil.WATER)) return;
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private static boolean placePart(BlockData orig, ChunkData chunk, Vector block, double thresh, Sampler sampler, Stairs stairNew) {
        if(sampler.sample(block.getBlockX() - 0.55, block.getBlockY(), block.getBlockZ()) > thresh) {
            stairNew.setFacing(BlockFace.WEST);
        } else if(sampler.sample(block.getBlockX(), block.getBlockY(), block.getBlockZ() - 0.55) > thresh) {
            stairNew.setFacing(BlockFace.NORTH);
        } else if(sampler.sample(block.getBlockX(), block.getBlockY(), block.getBlockZ() + 0.55) > thresh) {
            stairNew.setFacing(BlockFace.SOUTH);
        } else if(sampler.sample(block.getBlockX() + 0.55, block.getBlockY(), block.getBlockZ()) > thresh) {
            stairNew.setFacing(BlockFace.EAST);
        } else stairNew = null;
        if(stairNew != null) {
            if(orig.matches(DataUtil.WATER)) stairNew.setWaterlogged(true);
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), stairNew);
            return true;
        }
        return false;
    }

    @Override
    public void attachProfiler(WorldProfiler p) {
        super.attachProfiler(p);
        popMan.attachProfiler(p);
    }

    @Override
    @SuppressWarnings("try")
    public ChunkData generateBase(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, ChunkInterpolator interpolator) {
        if(needsLoad) load(world); // Load population data for world.
        ChunkData chunk = createChunkData(world);
        TerraWorld tw = TerraWorld.getWorld(world);
        if(!tw.isSafe()) return chunk;
        int xOrig = (chunkX << 4);
        int zOrig = (chunkZ << 4);
        org.polydev.gaea.biome.BiomeGrid grid = getBiomeGrid(world);

        ElevationInterpolator elevationInterpolator;
        try(ProfileFuture ignore = TerraProfiler.fromWorld(world).measure("ElevationTime")) {
            elevationInterpolator = new ElevationInterpolator(chunkX, chunkZ, tw.getGrid());
        }

        Sampler sampler = new Sampler(interpolator, elevationInterpolator);

        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                int paletteLevel = 0;

                int cx = xOrig + x;
                int cz = zOrig + z;

                Biome b = grid.getBiome(xOrig + x, zOrig + z, GenerationPhase.PALETTE_APPLY);
                BiomeTemplate c = ((UserDefinedBiome) b).getConfig();

                int sea = c.getSeaLevel();
                Palette<BlockData> seaPalette = c.getOceanPalette();

                boolean justSet = false;
                BlockData data = null;
                for(int y = world.getMaxHeight() - 1; y >= 0; y--) {
                    if(sampler.sample(x, y, z) > 0) {
                        justSet = true;
                        data = getPalette(x, y, z, c, sampler).get(paletteLevel, cx, cz);
                        chunk.setBlock(x, y, z, data);
                        if(paletteLevel == 0 && c.doSlabs() && y < 255) {
                            prepareBlockPartFloor(data, chunk.getBlockData(x, y + 1, z), chunk, new Vector(x, y + 1, z), c.getSlabPalettes(),
                                    c.getStairPalettes(), c.getSlabThreshold(), sampler);
                        }
                        paletteLevel++;
                    } else if(y <= sea) {
                        justSet = false;
                        chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, z + zOrig));
                        paletteLevel = 0;
                    } else {
                        if(justSet && c.doSlabs()) {
                            prepareBlockPartCeiling(data, chunk.getBlockData(x, y, z), chunk, new Vector(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
                        }
                        justSet = false;
                        paletteLevel = 0;
                    }
                }
            }
        }
        return chunk;
    }


    private void load(World w) {
        try {
            popMan.loadBlocks(w);
        } catch(FileNotFoundException e) {
            LangUtil.log("warning.no-population", Level.WARNING);
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        popMap.put(w, popMan);
        needsLoad = false;
    }

    @Override
    public int getNoiseOctaves(World world) {
        return 1;
    }

    @Override
    public double getNoiseFrequency(World world) {
        return 0.02;
    }

    @Override
    public List<GenerationPopulator> getGenerationPopulators(World world) {
        return Collections.emptyList();
    }


    @Override
    public org.polydev.gaea.biome.BiomeGrid getBiomeGrid(World world) {
        return TerraWorld.getWorld(world).getGrid();
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Arrays.asList(new CavePopulator(), new StructurePopulator(), popMan);
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

}
