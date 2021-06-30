package com.dfsek.terra.config.templates;

import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.math.paralithic.BlankFunction;
import com.dfsek.terra.api.math.paralithic.defined.UserDefinedFunction;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.palette.PaletteImpl;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.api.world.palette.slant.SlantHolder;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;
import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.world.population.items.TerraStructure;
import com.dfsek.terra.world.population.items.flora.FloraLayer;
import com.dfsek.terra.world.population.items.ores.OreHolder;
import com.dfsek.terra.world.population.items.tree.TreeLayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomeTemplate extends AbstractableTemplate implements ValidatedConfigTemplate {
    private final ConfigPackImpl pack;

    @Value("id")
    private String id;

    @Value("extends")
    @Default
    private List<String> extended = Collections.emptyList();

    @Value("variables")
    @Abstractable
    @Default
    private Map<String, Double> variables = new HashMap<>();

    @Value("functions")
    @Default
    @Abstractable
    private LinkedHashMap<String, FunctionTemplate> functions = new LinkedHashMap<>();

    @Value("beta.carving.equation")
    @Abstractable
    @Default
    private String carvingEquation = "0";

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
    private Palette oceanPalette;

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

    @Value("carving")
    @Abstractable
    @Default
    private Map<UserDefinedCarver, Integer> carvers = new HashMap<>();

    @Value("colors")
    @Abstractable
    @Default
    private Map<String, Integer> colors = new HashMap<>(); // Plain ol' map, so platforms can decide what to do with colors (if anything).

    public List<String> getExtended() {
        return extended;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Map<String, Integer> getColors() {
        return colors;
    }

    public Map<UserDefinedCarver, Integer> getCarvers() {
        return carvers;
    }

    public Map<String, FunctionTemplate> getFunctions() {
        return functions;
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

    public List<FloraLayer> getFlora() {
        return flora;
    }

    public boolean doSlabs() {
        return doSlabs;
    }

    public BiomeTemplate(ConfigPackImpl pack, TerraPlugin main) {
        this.pack = pack;
        biomeNoise = new NoiseSeeded() {
            @Override
            public NoiseSampler apply(Long seed) {
                return NoiseSampler.zero();
            }

            @Override
            public int getDimensions() {
                return 2;
            }
        };
        oceanPalette = new PaletteImpl.Singleton(main.getWorldHandle().createBlockData("minecraft:water"));
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

    public Palette getOceanPalette() {
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
        color |= 0xff000000; // Alpha adjustment
        Parser tester = new Parser();
        Scope testScope = new Scope().withParent(pack.getVarScope());

        variables.forEach(testScope::create);

        testScope.addInvocationVariable("x");
        testScope.addInvocationVariable("y");
        testScope.addInvocationVariable("z");


        pack.getTemplate().getNoiseBuilderMap().forEach((id, builder) -> tester.registerFunction(id, new BlankFunction(builder.getDimensions()))); // Register dummy functions

        Map<String, FunctionTemplate> testFunctions = new LinkedHashMap<>(pack.getTemplate().getFunctions());
        testFunctions.putAll(functions);
        for(Map.Entry<String, FunctionTemplate> entry : testFunctions.entrySet()) {
            try {
                tester.registerFunction(entry.getKey(), UserDefinedFunction.newInstance(entry.getValue(), tester, testScope));
            } catch(ParseException e) {
                throw new ValidationException("Invalid function: ", e);
            }
        }

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
