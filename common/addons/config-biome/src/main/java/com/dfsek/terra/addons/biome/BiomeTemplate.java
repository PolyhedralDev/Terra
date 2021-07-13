package com.dfsek.terra.addons.biome;

import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.addons.biome.holder.PaletteHolder;
import com.dfsek.terra.addons.biome.slant.SlantHolder;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.structure.ConfiguredStructure;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.generator.Palette;

import java.util.ArrayList;
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
    private String id;

    @Value("extends")
    @Default
    private List<String> extended = Collections.emptyList();

    @Value("variables")
    @Abstractable
    @Default
    private Map<String, Double> variables = new HashMap<>();

    @Value("beta.carving.equation")
    @Abstractable
    @Default
    private NoiseSeeded carvingEquation = NoiseSeeded.zero(3);

    @Value("palette")
    @Abstractable
    private PaletteHolder palette;

    @Value("slant")
    @Abstractable
    @Default
    private SlantHolder slant = null;

    @Value("vanilla")
    @Abstractable
    private ProbabilityCollection<Biome> vanilla;

    @Value("biome-noise")
    @Default
    @Abstractable
    private NoiseSeeded biomeNoise = NoiseSeeded.zero(2);

    @Value("blend.distance")
    @Abstractable
    @Default
    private int blendDistance = 3;

    @Value("blend.weight")
    @Abstractable
    @Default
    private double blendWeight = 1;

    @Value("blend.step")
    @Abstractable
    @Default
    private int blendStep = 4;

    @Value("structures")
    @Abstractable
    @Default
    private List<ConfiguredStructure> structures = new ArrayList<>();

    @Value("noise")
    @Abstractable
    private NoiseSeeded noiseEquation;

    /*@Value("ores")
    @Abstractable
    @Default
    private OreHolder oreHolder = new OreHolder();*/

    @Value("ocean.level")
    @Abstractable
    @Default
    private int seaLevel = 62;

    @Value("ocean.palette")
    @Abstractable
    private Palette oceanPalette;

    @Value("elevation.equation")
    @Default
    @Abstractable
    private NoiseSeeded elevationEquation = NoiseSeeded.zero(2);

    @Value("elevation.weight")
    @Default
    @Abstractable
    private double elevationWeight = 1;

    /*@Value("flora")
    @Abstractable
    @Default
    private List<FloraLayer> flora = new ArrayList<>();

    @Value("trees")
    @Abstractable
    @Default
    private List<TreeLayer> trees = new ArrayList<>();*/

    @Value("slabs.enable")
    @Abstractable
    @Default
    private boolean doSlabs = false;

    @Value("slabs.threshold")
    @Abstractable
    @Default
    private double slabThreshold = 0.0075D;

    @Value("slabs.palettes")
    @Abstractable
    @Default
    private Map<BlockType, Palette> slabPalettes;

    @Value("slabs.stair-palettes")
    @Abstractable
    @Default
    private Map<BlockType, Palette> stairPalettes;

    @Value("interpolate-elevation")
    @Abstractable
    @Default
    private boolean interpolateElevation = true;

    @Value("color")
    @Default
    private int color = 0;

    @Value("tags")
    @Default
    @Abstractable
    private Set<String> tags = new HashSet<>();

    /*@Value("carving")
    @Abstractable
    @Default
    private Map<UserDefinedCarver, Integer> carvers = new HashMap<>();*/

    @Value("colors")
    @Abstractable
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

    public SlantHolder getSlant() {
        return slant;
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

    public NoiseSeeded getBiomeNoise() {
        return biomeNoise;
    }

    public NoiseSeeded getElevationEquation() {
        return elevationEquation;
    }

    public NoiseSeeded getCarvingEquation() {
        return carvingEquation;
    }

    public ConfigPack getPack() {
        return pack;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public Palette getOceanPalette() {
        return oceanPalette;
    }

    public String getID() {
        return id;
    }

    public PaletteHolder getPalette() {
        return palette;
    }

    public ProbabilityCollection<Biome> getVanilla() {
        return vanilla;
    }

    public List<ConfiguredStructure> getStructures() {
        return structures;
    }

    public NoiseSeeded getNoiseEquation() {
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
        Parser tester = new Parser();
        Scope testScope = new Scope();

        variables.forEach(testScope::create);

        testScope.addInvocationVariable("x");
        testScope.addInvocationVariable("y");
        testScope.addInvocationVariable("z");


        //pack.getTemplate().getNoiseBuilderMap().forEach((id, builder) -> tester.registerFunction(id, new BlankFunction(builder.getDimensions()))); // Register dummy functions

        try {
            noiseEquation.apply(0L);
        } catch(Exception e) {
            throw new ValidationException("Invalid noise sampler: ", e);
        }

        try {
            carvingEquation.apply(0L);
        } catch(Exception e) {
            throw new ValidationException("Invalid carving sampler: ", e);
        }

        try {
            elevationEquation.apply(0L);
        } catch(Exception e) {
            throw new ValidationException("Invalid elevation sampler: ", e);
        }

        return true;
    }
}
