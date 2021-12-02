/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.generators;

import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.WritableWorld;

import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.dfsek.terra.addons.chunkgenerator.PaletteUtil;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.Sampler3D;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteInfo;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.math.Sampler;
import com.dfsek.terra.api.world.biome.GenerationSettings;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


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
    
    @Override
    @SuppressWarnings("try")
    public void generateChunkData(@NotNull ProtoChunk chunk, @NotNull WritableWorld world,
                                  int chunkZ,
                                  int chunkX) {
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
                    
                    GenerationSettings generationSettings = biome.getGenerator();
                    
                    int sea = paletteInfo.getSeaLevel();
                    Palette seaPalette = paletteInfo.getOcean();
                    
                    BlockState data;
                    for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                        if(sampler.sample(x, y, z) > 0) {
                            data = PaletteUtil.getPalette(x, y, z, generationSettings, sampler, paletteInfo).get(paletteLevel, cx, y, cz,
                                                                                                                 seed);
                            chunk.setBlock(x, y, z, data);
                            
                            paletteLevel++;
                        } else if(y <= sea) {
                            chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, y, z + zOrig, seed));
                            paletteLevel = 0;
                        } else {
                            paletteLevel = 0;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public Sampler createSampler(int chunkX, int chunkZ, BiomeProvider provider, ServerWorld world, int elevationSmooth) {
        return new Sampler3D(chunkX, chunkZ, provider, world, elevationSmooth);
    }
    
    @Override
    public ConfigPack getConfigPack() {
        return configPack;
    }
    
    @Override
    public List<GenerationStage> getGenerationStages() {
        return generationStages;
    }
    
    @Override
    public BlockState getBlock(ServerWorld world, int x, int y, int z) {
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
}
