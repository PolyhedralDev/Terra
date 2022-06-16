/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation;


import com.dfsek.terra.addons.chunkgenerator.config.palette.PaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.generation.math.PaletteUtil;
import com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation.LazilyEvaluatedInterpolator;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.Sampler3D;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.SamplerProvider;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;
import com.dfsek.terra.api.world.info.WorldProperties;

import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;


public class NoiseChunkGenerator3D implements ChunkGenerator {
    private final Platform platform;
    
    private final SamplerProvider samplerCache;
    
    private final BlockState air;
    
    private final int carverHorizontalResolution;
    private final int carverVerticalResolution;
    
    private final int paletteRes;
    
    private final NoiseSampler paletteBlendSampler;
    
    private final double paletteBlendAmplitude;
    
    public NoiseChunkGenerator3D(Platform platform, int elevationBlend, int carverHorizontalResolution,
                                 int carverVerticalResolution, int paletteRes, NoiseSampler paletteBlendSampler,
                                 double paletteBlendAmplitude) {
        this.platform = platform;
        this.air = platform.getWorldHandle().air();
        this.carverHorizontalResolution = carverHorizontalResolution;
        this.carverVerticalResolution = carverVerticalResolution;
        this.paletteRes = paletteRes;
        this.paletteBlendSampler = paletteBlendSampler;
        this.paletteBlendAmplitude = paletteBlendAmplitude;
        this.samplerCache = new SamplerProvider(platform, elevationBlend);
    }
    
    private Biome getBiome(int min, int max, double noiseX, double noiseZ, BiomeProvider biomeProvider, int x, int y, int z, long seed) {
        if(paletteBlendAmplitude == 1) {
            return biomeProvider.getBiome(x, y, z, seed);
        }
        int mx = ((x + (int) (paletteBlendAmplitude * noiseX)) / paletteRes) * paletteRes;
        int my = ((y + (int) (paletteBlendAmplitude * paletteBlendSampler.noise(seed + 2, x, y, z))) / paletteRes) * paletteRes;
        int mz = ((z + (int) (paletteBlendAmplitude * noiseZ)) / paletteRes) * paletteRes;
        
        return biomeProvider.getBiome(mx, MathUtil.clamp(min, my, max), mz, seed);
    }
    
    @Override
    @SuppressWarnings("try")
    public void generateChunkData(@NotNull ProtoChunk chunk, @NotNull WorldProperties world,
                                  @NotNull BiomeProvider biomeProvider,
                                  int chunkX, int chunkZ) {
        platform.getProfiler().push("chunk_base_3d");
        int xOrig = (chunkX << 4);
        int zOrig = (chunkZ << 4);
        int min = world.getMinHeight();
        int max = world.getMaxHeight() - 1;
        
        Sampler3D sampler = samplerCache.getChunk(chunkX, chunkZ, world, biomeProvider);
        
        long seed = world.getSeed();
        
        LazilyEvaluatedInterpolator carver = new LazilyEvaluatedInterpolator(biomeProvider,
                                                                             chunkX,
                                                                             chunkZ,
                                                                             world.getMaxHeight(),
                                                                             world.getMinHeight(),
                                                                             carverHorizontalResolution,
                                                                             carverVerticalResolution,
                                                                             seed);
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                double paletteNoiseX = paletteBlendSampler.noise(seed, x, z);
                double paletteNoiseZ = paletteBlendSampler.noise(seed + 1, x, z);
                int paletteLevel = 0;
                
                int cx = xOrig + x;
                int cz = zOrig + z;
                
                BlockState data;
                for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                    Biome biome = getBiome(min, max, paletteNoiseX, paletteNoiseZ, biomeProvider, cx, y, cz, seed);
                    
                    PaletteInfo paletteInfo = biome.getContext().get(PaletteInfo.class);
                    
                    int sea = paletteInfo.seaLevel();
                    Palette seaPalette = paletteInfo.ocean();
                    
                    if(sampler.sample(x, y, z) > 0) {
                        if(carver.sample(x, y, z) <= 0) {
                            data = PaletteUtil
                                    .getPalette(x, y, z, sampler, paletteInfo, paletteLevel)
                                    .get(paletteLevel, cx, y, cz, seed);
                            chunk.setBlock(x, y, z, data);
                            paletteLevel++;
                        } else if(paletteInfo.updatePaletteWhenCarving()) {
                            paletteLevel = 0;
                        } else {
                            paletteLevel++;
                        }
                    } else if(y <= sea) {
                        chunk.setBlock(x, y, z, seaPalette.get(sea - y, x + xOrig, y, z + zOrig, seed));
                        paletteLevel = 0;
                    } else {
                        paletteLevel = 0;
                    }
                }
            }
        }
        platform.getProfiler().pop("chunk_base_3d");
    }
    
    @Override
    public BlockState getBlock(WorldProperties world, int x, int y, int z, BiomeProvider biomeProvider) {
        Biome biome = biomeProvider.getBiome(x, y, z, world.getSeed());
        Sampler3D sampler = samplerCache.get(x, z, world, biomeProvider);
        
        PaletteInfo paletteInfo = biome.getContext().get(PaletteInfo.class);
        
        int fdX = FastMath.floorMod(x, 16);
        int fdZ = FastMath.floorMod(z, 16);
        
        Palette palette = PaletteUtil.getPalette(fdX, y, fdZ, sampler, paletteInfo, 0);
        double noise = sampler.sample(fdX, y, fdZ);
        if(noise > 0) {
            int level = 0;
            for(int yi = world.getMaxHeight() - 1; yi > y; yi--) {
                if(sampler.sample(fdX, yi, fdZ) > 0) level++;
                else level = 0;
            }
            return palette.get(level, x, y, z, world.getSeed());
        } else if(y <= paletteInfo.seaLevel()) {
            return paletteInfo.ocean().get(paletteInfo.seaLevel() - y, x, y, z, world.getSeed());
        } else return air;
    }
    
    @Override
    public Palette getPalette(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        return biomeProvider.getBiome(x, y, z, world.getSeed()).getContext().get(PaletteInfo.class).paletteHolder().getPalette(y);
    }
    
    public SamplerProvider samplerProvider() {
        return samplerCache;
    }
}
