package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.structure.TerraStructure;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.world.palette.Palette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomeTemplate implements ConfigTemplate {

    private final ConfigPack pack;
    @Value("id")
    private String id;
    @Value("palette")
    @Abstractable
    private PaletteHolder palette;
    @Value("slant.palette")
    @Abstractable
    private PaletteHolder slantPalette;
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
    private List<Ore> ores = new GlueList<>();
    @Value("ocean.level")
    @Default
    private int seaLevel = 62;
    @Value("ocean.palette")
    @Default
    private Palette<BlockData> oceanPalette; // TODO: default palette
    @Value("slant.y-offset.top")
    @Default
    private double slantOffsetTop = 0.5D;
    @Value("slant.y-offset-bottom")
    @Default
    private double slantOffsetBottom = 0.25;
    @Value("elevation.equation")
    @Default
    @Abstractable
    private String elevationEquation = null;

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

    public List<Ore> getOres() {
        return ores;
    }

    public double getSlantOffsetTop() {
        return slantOffsetTop;
    }

    public double getSlantOffsetBottom() {
        return slantOffsetBottom;
    }
}
