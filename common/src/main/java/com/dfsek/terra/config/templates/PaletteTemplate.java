package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.generation.config.NoiseBuilder;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class PaletteTemplate extends AbstractableTemplate {
    @Value("noise")
    @Abstractable
    @Default
    private NoiseBuilder noise = new NoiseBuilder();

    @Value("id")
    private String id;

    @Value("layers")
    @Abstractable
    private List<PaletteLayer> palette;

    public PaletteTemplate() {
        noise.setType(FastNoiseLite.NoiseType.WhiteNoise);
    }

    public String getID() {
        return id;
    }

    public List<PaletteLayer> getPalette() {
        return palette;
    }

    public NoiseBuilder getNoise() {
        return noise;
    }
}
