/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation;


import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.addons.chunkgenerator.config.noise.BiomeNoiseProperties;
import com.dfsek.terra.addons.chunkgenerator.config.palette.PaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.generation.math.PaletteUtil;
import com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation.LazilyEvaluatedInterpolator;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.Sampler3D;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.SamplerProvider;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;
import com.dfsek.terra.api.world.info.WorldProperties;


public class NoiseChunkGenerator3D implements ChunkGenerator {
    private final Platform platform;
    
    private final SamplerProvider samplerCache;
    
    private final BlockState air;
    
    private final int carverHorizontalResolution;
    private final int carverVerticalResolution;
    
    private final PropertyKey<PaletteInfo> paletteInfoPropertyKey;
    private final PropertyKey<BiomeNoiseProperties> noisePropertiesKey;
    
    public NoiseChunkGenerator3D(ConfigPack pack, Platform platform, int elevationBlend, int carverHorizontalResolution,
                                 int carverVerticalResolution,
                                 PropertyKey<BiomeNoiseProperties> noisePropertiesKey,
                                 PropertyKey<PaletteInfo> paletteInfoPropertyKey) {
        this.platform = platform;
        this.air = platform.getWorldHandle().air();
        this.carverHorizontalResolution = carverHorizontalResolution;
        this.carverVerticalResolution = carverVerticalResolution;
        this.paletteInfoPropertyKey = paletteInfoPropertyKey;
        this.noisePropertiesKey = noisePropertiesKey;
        int maxBlend = pack
                .getBiomeProvider()
                .stream()
                .map(biome -> biome.getContext().get(noisePropertiesKey))
                .mapToInt(properties -> properties.blendDistance() * properties.blendStep())
                .max()
                .orElse(0);
        
        this.samplerCache = new SamplerProvider(platform, elevationBlend, noisePropertiesKey, maxBlend);
    }
    
    @Override
    @SuppressWarnings("try")
    public void generateChunkData(@NotNull ProtoChunk chunk, @NotNull WorldProperties world,
                                  @NotNull BiomeProvider biomeProvider,
                                  int chunkX, int chunkZ) {
        platform.getProfiler().push("chunk_base_3d");
        int xOrig = (chunkX << 4);
        int zOrig = (chunkZ << 4);
        
        Sampler3D sampler = samplerCache.getChunk(chunkX, chunkZ, world, biomeProvider);
        
        long seed = world.getSeed();
        
        LazilyEvaluatedInterpolator carver = new LazilyEvaluatedInterpolator(biomeProvider,
                                                                             chunkX,
                                                                             chunkZ,
                                                                             world.getMaxHeight(),
                                                                             noisePropertiesKey, world.getMinHeight(),
                                                                             carverHorizontalResolution,
                                                                             carverVerticalResolution,
                                                                             seed);
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                int paletteLevel = 0;
                
                int cx = xOrig + x;
                int cz = zOrig + z;
                
                BlockState data;
                Column<Biome> biomeColumn = biomeProvider.getColumn(cx, cz, world);
                for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                    Biome biome = biomeColumn.get(y);
                    
                    PaletteInfo paletteInfo = biome.getContext().get(paletteInfoPropertyKey);
                    
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
        
        PaletteInfo paletteInfo = biome.getContext().get(paletteInfoPropertyKey);
        
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
        return biomeProvider.getBiome(x, y, z, world.getSeed()).getContext().get(paletteInfoPropertyKey).paletteHolder().getPalette(y);
    }
    
    public SamplerProvider samplerProvider() {
        return samplerCache;
    }
}
