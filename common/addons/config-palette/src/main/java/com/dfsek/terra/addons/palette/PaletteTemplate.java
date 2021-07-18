package com.dfsek.terra.addons.palette;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.palette.palette.PaletteLayerHolder;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class PaletteTemplate implements AbstractableTemplate {
    @Value("noise")
    @Default
    private SeededNoiseSampler noise = SeededNoiseSampler.zero(2);

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

    public SeededNoiseSampler getNoise() {
        return noise;
    }
}
