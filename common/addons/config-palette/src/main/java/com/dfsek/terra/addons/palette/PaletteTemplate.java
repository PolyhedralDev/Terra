package com.dfsek.terra.addons.palette;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.palette.palette.PaletteLayerHolder;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class PaletteTemplate implements AbstractableTemplate {
    @Value("noise")
    @Default
    private NoiseSampler noise = NoiseSampler.zero();

    @Value("id")
    @Final
    private String id;

    @Value("layers")
    private List<PaletteLayerHolder> palette;

    public String getID() {
        return id;
    }

    public List<PaletteLayerHolder> getPalette() {
        return palette;
    }

    public NoiseSampler getNoise() {
        return noise;
    }
}
