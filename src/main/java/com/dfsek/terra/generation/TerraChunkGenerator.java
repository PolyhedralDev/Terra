package com.dfsek.terra.generation;

import com.dfsek.terra.Debug;
import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.StructurePopulator;
import com.dfsek.terra.population.TreePopulator;
import com.dfsek.terra.structure.StructureSpawnRequirement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
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
    private final PopulationManager popMan = new PopulationManager(Terra.getInstance());
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
        ConfigPack config = TerraWorld.getWorld(world).getConfig();
        int xOrig = (chunkX << 4);
        int zOrig = (chunkZ << 4);
        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                int paletteLevel = 0;
                int cx = xOrig + x;
                int cz = zOrig + z;
                Biome b = getBiomeGrid(world).getBiome(xOrig+x, zOrig+z, GenerationPhase.PALETTE_APPLY);
                BiomeConfig c = config.getBiome((UserDefinedBiome) b);
                int sea = c.getSeaLevel();
                Palette<BlockData> seaPalette = c.getOceanPalette();
                for(int y = world.getMaxHeight()-1; y >= 0; y--) {
                    if(super.getInterpolatedNoise(x, y, z) > 0) {
                        BlockData data = b.getGenerator().getPalette(y).get(paletteLevel, cx, cz);
                        chunk.setBlock(x, y, z, data);
                        if(paletteLevel == 0 && c.getSlabs() != null && y < 255) {
                            prepareBlockPart(data, chunk.getBlockData(x, y+1, z), chunk, new Vector(x, y+1, z), c.getSlabs(), c.getStairs(), c.getSlabThreshold());
                        }
                        paletteLevel++;
                    } else if(y <= sea) {
                        chunk.setBlock(x, y, z, seaPalette.get(sea-y, x+xOrig, z+zOrig));
                        paletteLevel = 0;
                    } else paletteLevel = 0;
                }
            }
        }
        return chunk;
    }

    private void prepareBlockPart(BlockData down, BlockData orig, ChunkData chunk, Vector block, Map<Material, Palette<BlockData>> slabs, Map<Material, Palette<BlockData>> stairs, double thresh) {
        double _11 = getInterpolatedNoise(block.getBlockX(), block.getBlockY() - 0.4, block.getBlockZ());
        if(_11 > thresh) {
            double _01 = getInterpolatedNoise(block.getBlockX() - 0.5, block.getBlockY(), block.getBlockZ());
            double _10 = getInterpolatedNoise(block.getBlockX(), block.getBlockY(), block.getBlockZ() - 0.5);
            double _12 = getInterpolatedNoise(block.getBlockX(), block.getBlockY(), block.getBlockZ() + 0.5);
            double _21 = getInterpolatedNoise(block.getBlockX() + 0.5, block.getBlockY(), block.getBlockZ());
            if(stairs != null) {
                Palette<BlockData> stairPalette = stairs.get(down.getMaterial());
                if(stairPalette != null) {
                    BlockData stair = stairPalette.get(0, block.getBlockX(), block.getBlockZ());
                    Stairs finalStair = getStair(new double[] {_01, _10, _12, _21}, (Stairs) stair, thresh);
                    if(finalStair != null) {
                        if(orig.matches(Util.WATER)) finalStair.setWaterlogged(true);
                        chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), finalStair);
                        return;
                    }
                }
            }
            BlockData slab = slabs.getOrDefault(down.getMaterial(), Util.BLANK_PALETTE).get(0, block.getBlockX(), block.getBlockZ());
            if(slab instanceof Waterlogged) {
                ((Waterlogged) slab).setWaterlogged(orig.matches(Util.WATER));
            } else if(orig.matches(Util.WATER)) return;
            chunk.setBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ(), slab);
        }
    }

    private static Stairs getStair(double[] vals, Stairs stair, double thresh) {
        if(vals.length != 4) throw new IllegalArgumentException();
        Stairs stairNew = (Stairs) stair.clone();
        if(vals[0] > thresh) {
            stairNew.setFacing(BlockFace.WEST);
        } else if(vals[1] > thresh) {
            stairNew.setFacing(BlockFace.NORTH);
        } else if(vals[2] > thresh) {
            stairNew.setFacing(BlockFace.SOUTH);
        } else if(vals[3] > thresh) {
            stairNew.setFacing(BlockFace.EAST);
        } else return null;
        return stairNew;
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
    public boolean shouldGenerateStructures() {
        return true;
    }


}
