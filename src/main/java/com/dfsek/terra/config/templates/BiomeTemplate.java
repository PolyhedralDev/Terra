package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.biome.palette.SinglePalette;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.generation.items.flora.FloraLayer;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.generation.items.ores.OreConfig;
import com.dfsek.terra.structure.TerraStructure;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.world.palette.Palette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomeTemplate extends AbstractableTemplate {

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
    @Value("erodible")
    @Abstractable
    @Default
    private boolean erodible = false;
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
    private Map<Ore, OreConfig> ores = new HashMap<>();
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

    public PaletteHolder getSlantPalette() {
        return slantPalette;
    }

    public Biome getVanilla() {
        return vanilla;
    }

    public boolean isErodible() {
        return erodible;
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

    public Map<Ore, OreConfig> getOres() {
        return ores;
    }
}
