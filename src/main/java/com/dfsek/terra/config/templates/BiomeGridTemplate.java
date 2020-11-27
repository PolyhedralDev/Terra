package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import org.polydev.gaea.biome.Biome;

import java.util.List;

@SuppressWarnings("unused")
public class BiomeGridTemplate implements ConfigTemplate {

    @Value("grid")
    @Abstractable
    private List<List<Biome>> grid;

    @Value("id")
    private String id;

    @Value("frequency.x")
    @Abstractable
    private double xFreq;

    @Value("frequency.z")
    @Abstractable
    private double zFreq;

    public String getID() {
        return id;
    }

    public List<List<Biome>> getGrid() {
        return grid;
    }

    public double getXFreq() {
        return xFreq;
    }

    public double getZFreq() {
        return zFreq;
    }
}
