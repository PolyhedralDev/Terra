package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.biome.palette.SinglePalette;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.generation.items.TerraStructure;
import com.dfsek.terra.generation.items.flora.FloraLayer;
import com.dfsek.terra.generation.items.ores.OreHolder;
import com.dfsek.terra.generation.items.tree.TreeLayer;
import com.dfsek.terra.math.BlankFunction;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.world.palette.Palette;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.tokenizer.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomeTemplate extends AbstractableTemplate implements ValidatedConfigTemplate {

    private final ConfigPack pack;
    @Value("id")
    private String id;
    @Value("palette")
    @Abstractable
    private PaletteHolder palette;
    @Value("slant.palette")
    @Abstractable
    @Default
    private PaletteHolder slantPalette = null;
    @Value("vanilla")
    @Abstractable
    private Biome vanilla;
    @Value("erode")
    @Abstractable
    @Default
    private String erode = null;
    @Value("structures")
    @Abstractable
    @Default
    private List<TerraStructure> structures = new GlueList<>();
    @Value("carving")
    @Abstractable
    @Default
    private Map<UserDefinedCarver, Integer> carvers = new HashMap<>();
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
    private Palette<BlockData> oceanPalette = new SinglePalette<>(Material.WATER.createBlockData());

    @Value("elevation.equation")
    @Default
    @Abstractable
    private String elevationEquation = null;

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
    private Map<Material, Palette<BlockData>> slabPalettes;

    @Value("stair-palettes")
    @Abstractable
    @Default
    private Map<Material, Palette<BlockData>> stairPalettes;

    @Value("slant.threshold")
    @Abstractable
    @Default
    private double slantThreshold = 0.1;

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

    public Map<Material, Palette<BlockData>> getSlabPalettes() {
        return slabPalettes;
    }

    public Map<Material, Palette<BlockData>> getStairPalettes() {
        return stairPalettes;
    }

    public BiomeTemplate(ConfigPack pack) {
        this.pack = pack;
    }

    public String getElevationEquation() {
        return elevationEquation;
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

    public Biome getVanilla() {
        return vanilla;
    }

    public String getErode() {
        return erode;
    }

    public List<TerraStructure> getStructures() {
        return structures;
    }

    public Map<UserDefinedCarver, Integer> getCarvers() {
        return carvers;
    }

    public String getNoiseEquation() {
        return noiseEquation;
    }

    public OreHolder getOreHolder() {
        return oreHolder;
    }

    @Override
    public boolean validate() throws ValidationException {
        Parser tester = new Parser();
        Scope testScope = new Scope().withParent(pack.getVarScope());
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
            if(elevationEquation != null) tester.parse(elevationEquation, testScope);
        } catch(ParseException e) {
            throw new ValidationException("Invalid elevation equation: ", e);
        }

        return true;
    }
}
