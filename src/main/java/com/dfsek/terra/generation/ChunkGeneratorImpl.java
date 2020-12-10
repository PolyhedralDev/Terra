package com.dfsek.terra.generation;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.gaea.biome.Biome;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import com.dfsek.terra.api.gaea.generation.GenerationPopulator;
import com.dfsek.terra.api.gaea.math.ChunkInterpolator3;
import com.dfsek.terra.api.gaea.population.PopulationManager;
import com.dfsek.terra.api.gaea.profiler.ProfileFuture;
import com.dfsek.terra.api.gaea.profiler.WorldProfiler;
import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.api.generic.generator.BlockPopulator;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.world.BiomeGrid;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.TreePopulator;
import com.dfsek.terra.util.PaletteUtil;
import com.dfsek.terra.util.SlabUtil;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class ChunkGeneratorImpl implements ChunkGenerator {
    private static final Map<org.bukkit.World, PopulationManager> popMap = new HashMap<>();
    private final PopulationManager popMan;
    private final ConfigPack configPack;
    private final Terra main;
    private boolean needsLoad = true;

    public ChunkGeneratorImpl(ConfigPack c, Terra main) {
        popMan = new PopulationManager(main);
        this.configPack = c;
        this.main = main;
        popMan.attach(new OrePopulator(main));
        popMan.attach(new TreePopulator(main));
        popMan.attach(new FloraPopulator(main));
    }

    public static synchronized void saveAll() {
        for(Map.Entry<org.bukkit.World, PopulationManager> e : popMap.entrySet()) {
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
    public void generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome, ChunkData chunk) {
        TerraWorld tw = main.getWorld(world);
        try(ProfileFuture ignore = tw.getProfiler().measure("TotalChunkGenTime")) {
            ChunkInterpolator3 interp;
            try(ProfileFuture ignored = tw.getProfiler().measure("ChunkBaseGenTime")) {
                interp = new ChunkInterpolator3(world, x, z, tw.getGrid());
                if(needsLoad) load(world); // Load population data for world.

                if(!tw.isSafe()) return;
                int xOrig = (x << 4);
                int zOrig = (z << 4);
                com.dfsek.terra.api.gaea.biome.BiomeGrid grid = tw.getGrid();

                ElevationInterpolator elevationInterpolator;
                try(ProfileFuture ignored1 = tw.getProfiler().measure("ElevationTime")) {
                    elevationInterpolator = new ElevationInterpolator(x, z, tw.getGrid());
                }

                Sampler sampler = new Sampler(interp, elevationInterpolator);

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
                                    SlabUtil.prepareBlockPartFloor(data, chunk.getBlockData(x, y + 1, z), chunk, new Vector3(x, y + 1, z), c.getSlabPalettes(),
                                            c.getStairPalettes(), c.getSlabThreshold(), sampler);
                                }
                                paletteLevel++;
                            } else if(y <= sea) {
                                chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, z + zOrig));
                                if(justSet && c.doSlabs()) {
                                    SlabUtil.prepareBlockPartCeiling(data, chunk.getBlockData(x, y, z), chunk, new Vector3(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
                                }
                                justSet = false;
                                paletteLevel = 0;
                            } else {
                                if(justSet && c.doSlabs()) {
                                    SlabUtil.prepareBlockPartCeiling(data, chunk.getBlockData(x, y, z), chunk, new Vector3(x, y, z), c.getSlabPalettes(), c.getStairPalettes(), c.getSlabThreshold(), sampler);
                                }
                                justSet = false;
                                paletteLevel = 0;
                            }
                        }
                    }
                }
            }
            try(ProfileFuture ignored = measure("BiomeApplyTime")) {
                com.dfsek.terra.api.gaea.biome.BiomeGrid grid = getBiomeGrid(world);
                int xOrig = (chunkX << 4);
                int zOrig = (chunkZ << 4);
                for(byte x = 0; x < 4; x++) {
                    for(byte z = 0; z < 4; z++) {
                        int cx = xOrig + (x << 2);
                        int cz = zOrig + (z << 2);
                        Biome b = grid.getBiome(cx, cz, GenerationPhase.PALETTE_APPLY);
                        biome.setBiome(x << 2, z << 2, b.getVanillaBiome());
                    }
                }
            }
            for(GenerationPopulator g : getGenerationPopulators(world)) {
                g.populate(world, chunk, random, chunkX, chunkZ, interp);
            }
        }


        return chunk;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.emptyList();
    }

    public void attachProfiler(WorldProfiler p) {
        popMan.attachProfiler(p);
    }

    private void load(org.bukkit.World w) {
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
}
