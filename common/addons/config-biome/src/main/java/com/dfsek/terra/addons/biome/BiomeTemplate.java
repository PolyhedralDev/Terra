package com.dfsek.terra.addons.biome;

import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;
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
    private String id;

    @Value("extends")
    @Final
    @Default
    private List<String> extended = Collections.emptyList();

    @Value("variables")
    @Default
    private Map<String, Double> variables = new HashMap<>();

    @Value("beta.carving.equation")
    @Default
    private NoiseSampler carvingEquation = NoiseSampler.zero();

    @Value("vanilla")
    private ProbabilityCollection<Biome> vanilla;

    @Value("biome-noise")
    @Default
    private NoiseSampler biomeNoise = NoiseSampler.zero();

    @Value("blend.distance")
    @Default
    private int blendDistance = 3;

    @Value("blend.weight")
    @Default
    private double blendWeight = 1;

    @Value("blend.step")
    @Default
    private int blendStep = 4;

    @Value("noise")
    private NoiseSampler noiseEquation;

    @Value("ocean.level")
    @Default
    private int seaLevel = 62;

    @Value("elevation.equation")
    @Default
    private NoiseSampler elevationEquation = NoiseSampler.zero();

    @Value("elevation.weight")
    @Default
    private double elevationWeight = 1;

    @Value("slabs.enable")
    @Default
    private boolean doSlabs = false;

    @Value("slabs.threshold")
    @Default
    private double slabThreshold = 0.0075D;

    @Value("slabs.palettes")
    @Default
    private Map<BlockType, Palette> slabPalettes;

    @Value("slabs.stair-palettes")
    @Default
    private Map<BlockType, Palette> stairPalettes;

    @Value("interpolate-elevation")
    @Default
    private boolean interpolateElevation = true;

    @Value("color")
    @Final
    @Default
    private int color = 0;

    @Value("tags")
    @Default
    private Set<String> tags = new HashSet<>();

    @Value("colors")
    @Default
    private Map<String, Integer> colors = new HashMap<>(); // Plain ol' map, so platforms can decide what to do with colors (if anything).

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
