package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.List;

@SuppressWarnings("unused")
public class BiomeGridTemplate extends AbstractableTemplate {
    @Value("id")
    private String id;

    /**
     * A 2D array of {@link TerraBiome} IDs that make up this grid.
     */
    @Value("grid")
    @Abstractable
    private List<List<TerraBiome>> grid;

    /**
     * X frequency of noise function
     */
    @Value("frequency.x")
    @Abstractable
    private double xFreq;

    /**
     * Z frequency of noise function
     */
    @Value("frequency.z")
    @Abstractable
    private double zFreq;

    public String getID() {
        return id;
    }

    public List<List<TerraBiome>> getGrid() {
        return grid;
    }

    public double getXFreq() {
        return xFreq;
    }

    public double getZFreq() {
        return zFreq;
    }
}
