package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.palette.holder.PaletteLayerHolder;
import com.dfsek.terra.world.generation.config.NoiseBuilder;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class PaletteTemplate extends AbstractableTemplate {
    @Value("noise")
    @Abstractable
    @Default
    private NoiseSeeded noise;

    @Value("id")
    private String id;

    @Value("layers")
    @Abstractable
    private List<PaletteLayerHolder> palette;

    public PaletteTemplate() {
        NoiseBuilder builder = new NoiseBuilder();
        builder.setType(FastNoiseLite.NoiseType.WhiteNoise);
        builder.setDimensions(3);
        this.noise = new NoiseSeeded() {
            @Override
            public NoiseSampler apply(Long seed) {
                return builder.build(seed);
            }

            @Override
            public int getDimensions() {
                return 3;
            }
        };
    }

    public String getID() {
        return id;
    }

    public List<PaletteLayerHolder> getPalette() {
        return palette;
    }

    public NoiseSeeded getNoise() {
        return noise;
    }
}
