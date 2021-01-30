package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.math.parsii.BlankFunction;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.palette.SinglePalette;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.generation.config.NoiseBuilder;
import com.dfsek.terra.world.population.items.TerraStructure;
import com.dfsek.terra.world.population.items.flora.FloraLayer;
import com.dfsek.terra.world.population.items.ores.OreHolder;
import com.dfsek.terra.world.population.items.tree.TreeLayer;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.tokenizer.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomeTemplate extends AbstractableTemplate implements ValidatedConfigTemplate {

    private final ConfigPack pack;

    @Value("id")
    private String id;

    @Value("extends")
    @Default
    private String extend = null;

    @Value("variables")
    @Abstractable
    @Default
    private Map<String, Double> variables = new HashMap<>();

    @Value("carving.equation")
    @Abstractable
    @Default
    private String carvingEquation = "0";

    @Value("palette")
    @Abstractable
    private PaletteHolder palette;

    @Value("slant.palette")
    @Abstractable
    @Default
    private PaletteHolder slantPalette = null;

    @Value("vanilla")
    @Abstractable
    private ProbabilityCollection<Biome> vanilla;

    @Value("biome-noise")
    @Default
    @Abstractable
    private NoiseSeeded biomeNoise;

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

    @Value("erode")
    @Abstractable
    @Default
    private String erode = null;

    @Value("structures")
    @Abstractable
    @Default
    private List<TerraStructure> structures = new GlueList<>();

    @Value("noise-equation")
    @Abstractable
    private String noiseEquation;

    @Value("ores")
    @Abstractable
    @Default
    private OreHolder oreHolder = new OreHolder();

    @Value("ocean.level")
    @Abstractable
    @Default
    private int seaLevel = 62;

    @Value("ocean.palette")
    @Abstractable
    @Default
    private Palette<BlockData> oceanPalette;

    @Value("elevation.equation")
    @Default
    @Abstractable
    private String elevationEquation = null;

    @Value("elevation.weight")
    @Default
    @Abstractable
    private double elevationWeight = 1;

    @Value("flora")
    @Abstractable
    @Default
    private List<FloraLayer> flora = new GlueList<>();

    @Value("trees")
    @Abstractable
    @Default
    private List<TreeLayer> trees = new GlueList<>();

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
    private Map<MaterialData, Palette<BlockData>> slabPalettes;

    @Value("slabs.stair-palettes")
    @Abstractable
    @Default
    private Map<MaterialData, Palette<BlockData>> stairPalettes;

    @Value("slant.threshold")
    @Abstractable
    @Default
    private double slantThreshold = 0.1;

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
    private Set<String> tags;

    public Set<String> getTags() {
        return tags;
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

    public String getExtend() {
        return extend;
    }

    public double getSlantThreshold() {
        return slantThreshold;
    }

    public double getSlabThreshold() {
        return slabThreshold;
    }

    public List<FloraLayer> getFlora() {
        return flora;
    }

    public boolean doSlabs() {
        return doSlabs;
    }

    public Map<MaterialData, Palette<BlockData>> getSlabPalettes() {
        return slabPalettes;
    }

    public Map<MaterialData, Palette<BlockData>> getStairPalettes() {
        return stairPalettes;
    }

    public BiomeTemplate(ConfigPack pack, TerraPlugin main) {
        this.pack = pack;
        NoiseBuilder builder = new NoiseBuilder();
        builder.setType(FastNoiseLite.NoiseType.Constant);
        biomeNoise = new NoiseSeeded() {
            @Override
            public NoiseSampler apply(Long seed) {
                return builder.build(seed);
            }

            @Override
            public int getDimensions() {
                return 2;
            }
        };
        oceanPalette = new SinglePalette<>(main.getWorldHandle().createBlockData("minecraft:water"));
    }

    public NoiseSeeded getBiomeNoise() {
        return biomeNoise;
    }

    public String getElevationEquation() {
        return elevationEquation;
    }

    public String getCarvingEquation() {
        return carvingEquation;
    }

    public ConfigPack getPack() {
        return pack;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public Palette<BlockData> getOceanPalette() {
        return oceanPalette;
    }

    public String getID() {
        return id;
    }

    public PaletteHolder getPalette() {
        return palette;
    }

    public List<TreeLayer> getTrees() {
        return trees;
    }

    public PaletteHolder getSlantPalette() {
        return slantPalette;
    }

    public ProbabilityCollection<Biome> getVanilla() {
        return vanilla;
    }

    public String getErode() {
        return erode;
    }

    public List<TerraStructure> getStructures() {
        return structures;
    }

    public String getNoiseEquation() {
        return noiseEquation;
    }

    public OreHolder getOreHolder() {
        return oreHolder;
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
        color |= 0x1fe00000; // Alpha adjustment
        Parser tester = new Parser();
        Scope testScope = new Scope().withParent(pack.getVarScope());

        variables.forEach((id, val) -> testScope.create(id).setValue(val));

        testScope.create("x");
        testScope.create("y");
        testScope.create("z");
        testScope.create("seed");

        pack.getTemplate().getNoiseBuilderMap().forEach((id, builder) -> tester.registerFunction(id, new BlankFunction(builder.getDimensions()))); // Register dummy functions

        try {
            tester.parse(noiseEquation, testScope);
        } catch(ParseException e) {
            throw new ValidationException("Invalid noise equation: ", e);
        }

        try {
            tester.parse(carvingEquation, testScope);
        } catch(ParseException e) {
            throw new ValidationException("Invalid carving equation: ", e);
        }

        try {
            if(elevationEquation != null) tester.parse(elevationEquation, testScope);
        } catch(ParseException e) {
            throw new ValidationException("Invalid elevation equation: ", e);
        }

        return true;
    }
}
