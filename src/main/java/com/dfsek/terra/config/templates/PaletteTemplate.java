package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.biome.palette.PaletteLayer;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class PaletteTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("layers")
    @Abstractable
    private List<PaletteLayer> palette;

    @Value("simplex")
    @Abstractable
    @Default
    private boolean simplex = false;

    @Value("frequency")
    @Abstractable
    @Default
    private double frequency = 0.02D;

    @Value("seed")
    @Abstractable
    @Default
    private long seed = 0;

    public String getID() {
        return id;
    }

    public double getFrequency() {
        return frequency;
    }

    public long getSeed() {
        return seed;
    }

    public List<PaletteLayer> getPalette() {
        return palette;
    }

    public boolean isSimplex() {
        return simplex;
    }
}
