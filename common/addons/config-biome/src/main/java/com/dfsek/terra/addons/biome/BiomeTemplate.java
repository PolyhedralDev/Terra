/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class BiomeTemplate implements AbstractableTemplate, ValidatedConfigTemplate {
    private final ConfigPack pack;
    
    @Value("id")
    @Final
    private @Meta String id;
    
    @Value("extends")
    @Final
    @Default
    private List<String> extended = Collections.emptyList();
    
    @Value("variables")
    @Default
    private @Meta Map<String, @Meta Double> variables = new HashMap<>();
    
    @Value("vanilla")
    private @Meta PlatformBiome vanilla;
    
    @Value("blend.distance")
    @Default
    private @Meta int blendDistance = 3;
    
    @Value("blend.weight")
    @Default
    private @Meta double blendWeight = 1;
    
    @Value("blend.step")
    @Default
    private @Meta int blendStep = 4;
    
    @Value("noise")
    private @Meta NoiseSampler noiseEquation;
    
    @Value("ocean.level")
    @Default
    private @Meta int seaLevel = 62;
    
    @Value("slabs.enable")
    @Default
    private @Meta boolean doSlabs = false;
    
    @Value("slabs.threshold")
    @Default
    private @Meta double slabThreshold = 0.0075D;
    
    @Value("slabs.palettes")
    @Default
    private @Meta Map<@Meta BlockType, @Meta Palette> slabPalettes;
    
    @Value("slabs.stair-palettes")
    @Default
    private @Meta Map<@Meta BlockType, @Meta Palette> stairPalettes;
    
    @Value("interpolate-elevation")
    @Default
    private @Meta boolean interpolateElevation = true;
    
    @Value("color")
    @Final
    @Default
    private @Meta int color = 0;
    
    @Value("tags")
    @Default
    private @Meta Set<@Meta String> tags = new HashSet<>();
    
    @Value("colors")
    @Default
    private @Meta Map<String, @Meta Integer> colors = new HashMap<>();
    // Plain ol' map, so platforms can decide what to do with colors (if anything).
    
    public BiomeTemplate(ConfigPack pack, Platform platform) {
        this.pack = pack;
    }
    
    public boolean interpolateElevation() {
        return interpolateElevation;
    }
    
    public boolean doSlabs() {
        return doSlabs;
    }
    
    @Override
    public boolean validate() throws ValidationException {
        color |= 0xff000000; // Alpha adjustment
        return true;
    }
    
    public List<String> getExtended() {
        return extended;
    }
    
    public Set<String> getTags() {
        return tags;
    }
    
    public Map<String, Integer> getColors() {
        return colors;
    }
    
    public double getBlendWeight() {
        return blendWeight;
    }
    
    public int getColor() {
        return color;
    }
    
    public int getBlendDistance() {
        return blendDistance;
    }
    
    public double getSlabThreshold() {
        return slabThreshold;
    }
    
    public Map<BlockType, Palette> getSlabPalettes() {
        return slabPalettes;
    }
    
    public Map<BlockType, Palette> getStairPalettes() {
        return stairPalettes;
    }
    
    public ConfigPack getPack() {
        return pack;
    }
    
    public int getSeaLevel() {
        return seaLevel;
    }
    
    public String getID() {
        return id;
    }
    
    public PlatformBiome getVanilla() {
        return vanilla;
    }
    
    public NoiseSampler getNoiseEquation() {
        return noiseEquation;
    }
    
    public int getBlendStep() {
        return blendStep;
    }
    
    public Map<String, Double> getVariables() {
        return variables;
    }
}
