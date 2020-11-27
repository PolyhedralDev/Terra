package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.world.palette.Palette;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class PaletteTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("palette")
    @Abstractable
    private Palette<BlockData> palette;

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

    public String getId() {
        return id;
    }

    public double getFrequency() {
        return frequency;
    }

    public long getSeed() {
        return seed;
    }

    public Palette<BlockData> getPalette() {
        return palette;
    }
}
