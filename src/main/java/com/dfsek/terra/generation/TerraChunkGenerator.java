package com.dfsek.terra.generation;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.StructurePopulator;
import com.dfsek.terra.population.TreePopulator;
import com.dfsek.terra.structure.StructureSpawnRequirement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.population.PopulationManager;
import org.polydev.gaea.world.palette.Palette;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TerraChunkGenerator extends GaeaChunkGenerator {
    private static final BlockData STONE = Material.STONE.createBlockData();
    private static final BlockData WATER = Material.WATER.createBlockData();
    private final PopulationManager popMan = new PopulationManager();
    private boolean needsLoad = true;
    private static final Map<World, PopulationManager> popMap = new HashMap<>();

    public TerraChunkGenerator() {
        super(ChunkInterpolator.InterpolationType.TRILINEAR);
        popMan.attach(new TreePopulator());
        popMan.attach(new FloraPopulator());
        popMan.attach(new OrePopulator());
    }

    @Override
    public ChunkData generateBase(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, FastNoise fastNoise) {
        if(needsLoad) load(world);
        StructureSpawnRequirement.putNoise(world, fastNoise); // Assign noise to world to be used for structures.
        ChunkData chunk = createChunkData(world);
        int xOrig = (chunkX << 4);
        int zOrig = (chunkZ << 4);
        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                Biome b = getBiomeGrid(world).getBiome(xOrig+x, zOrig+z, GenerationPhase.PALETTE_APPLY);
                BiomeConfig c = BiomeConfig.fromBiome((UserDefinedBiome) b);
                int sea = c.getSeaLevel();
                Palette<BlockData> seaPalette = c.getOceanPalette();
                for(int y = 0; y < 256; y++) {
                    if(super.getInterpolatedNoise(x, y, z) > 0) chunk.setBlock(x, y, z, STONE);
                    else if(y <= sea) {
                        chunk.setBlock(x, y, z, seaPalette.get(sea-y, x+xOrig, z+zOrig));
                    }
                }
            }
        }
        return chunk;
    }

    private void load(World w) {
        try {
            popMan.loadBlocks(w);
        } catch(IOException e) {
            if(e instanceof FileNotFoundException) {
                Bukkit.getLogger().warning("[Terra] No population chunks were loaded. If this is your first time starting your server with Terra, or if you are creating a new world, this is normal.");
            } else e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        popMap.put(w, popMan);
        needsLoad = false;
    }

    public static void saveAll() {
        for(Map.Entry<World, PopulationManager> e : popMap.entrySet()) {
            try {
                e.getValue().saveBlocks(e.getKey());
                Bukkit.getLogger().info("[Terra] Saved data for world " + e.getKey().getName());
            } catch(IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public int getNoiseOctaves(World world) {
        return 4;
    }
    @Override
    public float getNoiseFrequency(World world) {
        return 1f/96;
    }

    @Override
    public List<GenerationPopulator> getGenerationPopulators(World world) {
        return Collections.singletonList(new SlabGenerator());
    }


    @Override
    public org.polydev.gaea.biome.BiomeGrid getBiomeGrid(World world) {
        return TerraBiomeGrid.fromWorld(world);
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Arrays.asList(new CavePopulator(), new StructurePopulator(), popMan);
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }


}
