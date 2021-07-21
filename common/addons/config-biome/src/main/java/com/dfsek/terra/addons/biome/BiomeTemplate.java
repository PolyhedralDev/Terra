package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.generator.Palette;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
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

    @Value("beta.carving.equation")
    @Default
    private @Meta NoiseSampler carvingEquation = NoiseSampler.zero();

    @Value("vanilla")
    private @Meta ProbabilityCollection<Biome> vanilla;

    @Value("biome-noise")
    @Default
    private @Meta NoiseSampler biomeNoise = NoiseSampler.zero();

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

    @Value("elevation.equation")
    @Default
    private @Meta NoiseSampler elevationEquation = NoiseSampler.zero();

    @Value("elevation.weight")
    @Default
    private @Meta double elevationWeight = 1;

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
    private @Meta Map<String, @Meta Integer> colors = new HashMap<>(); // Plain ol' map, so platforms can decide what to do with colors (if anything).

    public BiomeTemplate(ConfigPack pack, TerraPlugin main) {
        this.pack = pack;
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

    public boolean interpolateElevation() {
        return interpolateElevation;
    }

    public double getSlabThreshold() {
        return slabThreshold;
    }

    public boolean doSlabs() {
        return doSlabs;
    }

    public Map<BlockType, Palette> getSlabPalettes() {
        return slabPalettes;
    }

    public Map<BlockType, Palette> getStairPalettes() {
        return stairPalettes;
    }

    public NoiseSampler getBiomeNoise() {
        return biomeNoise;
    }

    public NoiseSampler getElevationEquation() {
        return elevationEquation;
    }

    public NoiseSampler getCarvingEquation() {
        return carvingEquation;
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

    public ProbabilityCollection<Biome> getVanilla() {
        return vanilla;
    }

    public NoiseSampler getNoiseEquation() {
        return noiseEquation;
    }

    public double getElevationWeight() {
        return elevationWeight;
    }

    public int getBlendStep() {
        return blendStep;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }

    @Override
    public boolean validate() throws ValidationException {
        color |= 0xff000000; // Alpha adjustment
        return true;
    }
}
