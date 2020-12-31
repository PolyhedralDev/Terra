package com.dfsek.terra.config.base;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.image.ImageLoader;
import net.jafama.FastMath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ConfigPackTemplate implements ValidatedConfigTemplate {
    @Value("id")
    private String id;

    @Value("noise")
    private Map<String, NoiseBuilder> noiseBuilderMap;

    @Value("variables")
    @Default
    private Map<String, Double> variables = new HashMap<>();

    @Value("grids")
    private List<String> grids;

    @Value("frequencies.grid-x")
    @Default
    private int gridFreqX = 4096;

    @Value("frequencies.grid-z")
    @Default
    private int gridFreqZ = 4096;

    @Value("frequencies.zone")
    @Default
    private int zoneFreq = 2048;

    @Value("blend.enable")
    @Default
    private boolean blend = false;

    @Value("blend.frequency")
    @Default
    private double blendFreq = 0.1;

    @Value("blend.amplitude")
    @Default
    private double blendAmp = 4.0D;

    @Value("blend.terrain.base")
    @Default
    private int baseBlend = 4;


    @Value("blend.terrain.elevation")
    @Default
    private int elevationBlend = 4;


    @Value("erode.enable")
    @Default
    private boolean erode = false;

    @Value("erode.frequency")
    @Default
    private double erodeFreq = 0.001D;

    @Value("erode.threshold")
    @Default
    private double erodeThresh = 0.0015D;

    @Value("erode.octaves")
    @Default
    private int erodeOctaves = 5;

    @Value("vanilla.mobs")
    @Default
    private boolean vanillaMobs = true;

    @Value("vanilla.caves")
    @Default
    private boolean vanillaCaves = false;

    @Value("vanilla.decorations")
    @Default
    private boolean vanillaDecorations = false;

    @Value("vanilla.structures")
    @Default
    private boolean vanillaStructures = false;

    @Value("author")
    @Default
    private String author = "Anon Y. Mous";

    @Value("disable.sapling")
    @Default
    private boolean disableSaplings = false;

    @Value("version")
    @Default
    private String version = "0.1.0";

    @Value("grid-options.type")
    @Default
    private TerraBiomeGrid.Type gridType = TerraBiomeGrid.Type.STANDARD;

    @Value("grid-options.radial.radius")
    @Default
    private double radius = 1000D;

    @Value("grid-options.radial.internal-grid")
    @Default
    private String internalGrid = null;

    @Value("image.enable")
    @Default
    private boolean fromImage = false;

    @Value("image.channels.biome-x")
    @Default
    private ImageLoader.Channel biomeXChannel = ImageLoader.Channel.RED;
    @Value("image.channels.biome-z")
    @Default
    private ImageLoader.Channel biomeZChannel = ImageLoader.Channel.GREEN;
    @Value("image.channels.zone")
    @Default
    private ImageLoader.Channel zoneChannel = ImageLoader.Channel.BLUE;

    @Value("image")
    @Default
    private ImageLoader imageLoader = null;


    public String getVersion() {
        return version;
    }

    public boolean isDisableSaplings() {
        return disableSaplings;
    }

    public String getID() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public boolean vanillaMobs() {
        return vanillaMobs;
    }

    public boolean vanillaCaves() {
        return vanillaCaves;
    }

    public boolean vanillaDecorations() {
        return vanillaDecorations;
    }

    public boolean vanillaStructures() {
        return vanillaStructures;
    }

    public Map<String, NoiseBuilder> getNoiseBuilderMap() {
        return noiseBuilderMap;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }


    public List<String> getGrids() {
        return grids;
    }

    public int getGridFreqX() {
        return gridFreqX;
    }

    public int getGridFreqZ() {
        return gridFreqZ;
    }

    public int getZoneFreq() {
        return zoneFreq;
    }

    public boolean isBlend() {
        return blend;
    }

    public double getBlendFreq() {
        return blendFreq;
    }

    public double getBlendAmp() {
        return blendAmp;
    }

    public boolean isErode() {
        return erode;
    }

    public double getErodeFreq() {
        return erodeFreq;
    }

    public double getErodeThresh() {
        return erodeThresh;
    }

    public int getErodeOctaves() {
        return erodeOctaves;
    }

    public double getRadialGridRadius() {
        return radius;
    }

    public String getRadialInternalGrid() {
        return internalGrid;
    }

    public TerraBiomeGrid.Type getGridType() {
        return gridType;
    }

    public ImageLoader.Channel getBiomeXChannel() {
        return biomeXChannel;
    }

    public ImageLoader.Channel getBiomeZChannel() {
        return biomeZChannel;
    }

    public boolean isFromImage() {
        return fromImage;
    }

    public ImageLoader.Channel getZoneChannel() {
        return zoneChannel;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public int getBaseBlend() {
        return baseBlend;
    }

    public int getElevationBlend() {
        return elevationBlend;
    }

    @Override
    public boolean validate() throws ValidationException {
        if(gridType.equals(TerraBiomeGrid.Type.RADIAL) && internalGrid == null)
            throw new ValidationException("No internal BiomeGrid specified");
        if(biomeZChannel.equals(biomeXChannel) || zoneChannel.equals(biomeXChannel) || zoneChannel.equals(biomeZChannel))
            throw new ValidationException("2 objects share the same image channels: biome-x and biome-z");

        if(!MathUtil.equals(FastMath.log(baseBlend) / FastMath.log(2d), FastMath.round(FastMath.log(baseBlend) / FastMath.log(2d)))) {
            throw new ValidationException("Biome base blend value \"" + baseBlend + "\" is not a power of 2.");
        }
        if(!MathUtil.equals(FastMath.log(elevationBlend) / FastMath.log(2d), FastMath.round(FastMath.log(elevationBlend) / FastMath.log(2d)))) {
            throw new ValidationException("Biome elevation blend value \"" + baseBlend + "\" is not a power of 2.");
        }
        return true;
    }
}
