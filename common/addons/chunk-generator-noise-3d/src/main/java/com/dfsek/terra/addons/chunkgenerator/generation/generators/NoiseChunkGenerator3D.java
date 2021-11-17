package com.dfsek.terra.addons.chunkgenerator.generation.generators;

import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dfsek.terra.addons.chunkgenerator.PaletteUtil;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.Sampler3D;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteInfo;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.block.state.properties.enums.Direction;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.math.Sampler;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.BiomeGrid;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.GenerationSettings;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkData;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.GenerationStage;
import com.dfsek.terra.api.world.generator.Palette;


public class NoiseChunkGenerator3D implements ChunkGenerator {
    private final ConfigPack configPack;
    private final Platform platform;
    private final List<GenerationStage> generationStages = new ArrayList<>();
    
    private final BlockState air;
    
    public NoiseChunkGenerator3D(ConfigPack c, Platform platform) {
        this.configPack = c;
        this.platform = platform;
        this.air = platform.getWorldHandle().air();
        c.getStages().forEach(stage -> generationStages.add(stage.newInstance(c)));
    }
    
    @SuppressWarnings("try")
    static void biomes(@NotNull World world, int chunkX, int chunkZ, @NotNull BiomeGrid biome, Platform platform) {
        try(ProfileFrame ignore = platform.getProfiler().profile("biomes")) {
            int xOrig = (chunkX << 4);
            int zOrig = (chunkZ << 4);
            long seed = world.getSeed();
            BiomeProvider grid = world.getBiomeProvider();
            for(int x = 0; x < 4; x++) {
                for(int z = 0; z < 4; z++) {
                    int cx = xOrig + (x << 2);
                    int cz = zOrig + (z << 2);
                    TerraBiome b = grid.getBiome(cx, cz, seed);
                    
                    biome.setBiome(cx, cz, b.getVanillaBiomes().get(b.getGenerator().getBiomeNoise(), cx, 0, cz, world.getSeed()));
                }
            }
        }
    }
    
    @Override
    @SuppressWarnings("try")
    public ChunkData generateChunkData(@NotNull World world, Random random, int chunkX, int chunkZ, ChunkData chunk) {
        try(ProfileFrame ignore = platform.getProfiler().profile("chunk_base_3d")) {
            BiomeProvider grid = world.getBiomeProvider();
            
            int xOrig = (chunkX << 4);
            int zOrig = (chunkZ << 4);
            
            Sampler sampler = world.getConfig().getSamplerCache().getChunk(chunkX, chunkZ);
            
            long seed = world.getSeed();
            
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    int paletteLevel = 0;
                    
                    int cx = xOrig + x;
                    int cz = zOrig + z;
                    
                    TerraBiome biome = grid.getBiome(cx, cz, seed);
                    
                    PaletteInfo paletteInfo = biome.getContext().get(PaletteInfo.class);
                    
                    if(paletteInfo == null) {
                        platform.logger().info("null palette: " + biome.getID());
                    }
                    
                    GenerationSettings generationSettings = biome.getGenerator();
                    
                    int sea = paletteInfo.getSeaLevel();
                    Palette seaPalette = paletteInfo.getOcean();
                    
                    boolean justSet = false;
                    BlockState data = null;
                    for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                        if(sampler.sample(x, y, z) > 0) {
                            justSet = true;
                            
                            data = PaletteUtil.getPalette(x, y, z, generationSettings, sampler, paletteInfo).get(paletteLevel, cx, y, cz,
                                                                                                                 seed);
                            chunk.setBlock(x, y, z, data);
                            
                            paletteLevel++;
                        } else if(y <= sea) {
                            chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, y, z + zOrig, seed));
                            
                            justSet = false;
                            paletteLevel = 0;
                        } else {
                            
                            justSet = false;
                            paletteLevel = 0;
                        }
                    }
                }
            }
            return chunk;
        }
    }
    
    @Override
    public void generateBiomes(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        biomes(world, chunkX, chunkZ, biome, platform);
    }
    
    @Override
    public Sampler createSampler(int chunkX, int chunkZ, BiomeProvider provider, World world, int elevationSmooth) {
        return new Sampler3D(chunkX, chunkZ, provider, world, elevationSmooth);
    }
    
    @Override
    public ConfigPack getConfigPack() {
        return configPack;
    }
    
    @Override
    public Platform getPlatform() {
        return platform;
    }
    
    @Override
    public List<GenerationStage> getGenerationStages() {
        return generationStages;
    }
    
    @Override
    public BlockState getBlock(World world, int x, int y, int z) {
        BiomeProvider provider = world.getBiomeProvider();
        TerraBiome biome = provider.getBiome(x, z, world.getSeed());
        Sampler sampler = world.getConfig().getSamplerCache().get(x, z);
        
        PaletteInfo paletteInfo = biome.getContext().get(PaletteInfo.class);
        Palette palette = PaletteUtil.getPalette(x, y, z, biome.getGenerator(), sampler, paletteInfo);
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
}
