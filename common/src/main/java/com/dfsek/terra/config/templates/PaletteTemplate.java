package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.world.palette.holder.PaletteLayerHolder;
import com.dfsek.terra.world.generation.config.NoiseBuilder;

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
    private List<PaletteLayerHolder> palette;

    public PaletteTemplate() {
        noise.setType(FastNoiseLite.NoiseType.WhiteNoise);
        noise.setDimensions(3);
    }

    public String getID() {
        return id;
    }

    public List<PaletteLayerHolder> getPalette() {
        return palette;
    }

    public NoiseBuilder getNoise() {
        return noise;
    }
}
