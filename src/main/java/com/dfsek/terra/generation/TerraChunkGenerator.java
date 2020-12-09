package com.dfsek.terra.generation;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.StructurePopulator;
import com.dfsek.terra.population.TreePopulator;
import com.dfsek.terra.util.PaletteUtil;
import com.dfsek.terra.util.SlabUtil;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
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
    private final PopulationManager popMan;
    private final ConfigPack configPack;
    private boolean needsLoad = true;
    private final Terra main;

    public ConfigPack getConfigPack() {
        return configPack;
    }


    public TerraChunkGenerator(ConfigPack c, Terra main) {
        super(ChunkInterpolator.InterpolationType.TRILINEAR);
        popMan = new PopulationManager(main);
        this.configPack = c;
        this.main = main;
        popMan.attach(new OrePopulator(main));
        popMan.attach(new TreePopulator(main));
        popMan.attach(new FloraPopulator(main));
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
        TerraWorld tw = main.getWorld(world);
        if(!tw.isSafe()) return chunk;
        int xOrig = (chunkX << 4);
        int zOrig = (chunkZ << 4);
        org.polydev.gaea.biome.BiomeGrid grid = getBiomeGrid(world);

        ElevationInterpolator elevationInterpolator;
        try(ProfileFuture ignore = tw.getProfiler().measure("ElevationTime")) {
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
                        data = PaletteUtil.getPalette(x, y, z, c, sampler).get(paletteLevel, cx, cz);
                        chunk.setBlock(x, y, z, data);
                        if(paletteLevel == 0 && c.doSlabs() && y < 255) {
                            SlabUtil.prepareBlockPartFloor(data, chunk.getBlockData(x, y + 1, z), chunk, new Vector(x, y + 1, z), c.getSlabPalettes(),
                                    c.getStairPalettes(), c.getSlabThreshold(), sampler);
                        }
                        paletteLevel++;
                    } else if(y <= sea) {
                        chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, z + zOrig));
                        if(justSet && c.doSlabs()) {
                            SlabUtil.prepareBlockPartCeiling(data, chunk.getBlockData(x, y, z), chunk, new Vector(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
                        }
                        justSet = false;
                        paletteLevel = 0;
                    } else {
                        if(justSet && c.doSlabs()) {
                            SlabUtil.prepareBlockPartCeiling(data, chunk.getBlockData(x, y, z), chunk, new Vector(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
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
        return main.getWorld(world).getGrid();
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Arrays.asList(new CavePopulator(main), new StructurePopulator(main), popMan);
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
