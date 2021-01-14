package com.dfsek.terra.population.items;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;

public abstract class PlaceableLayer<T> {
    protected final double density;
    protected final Range level;
    protected final ProbabilityCollection<T> layer;
    protected final TerraPlugin main;
    protected final NoiseSampler noise;

    public PlaceableLayer(double density, Range level, ProbabilityCollection<T> layer, NoiseSampler noise, TerraPlugin main) {
        this.density = density;
        this.level = level;
        this.layer = layer;
        this.main = main;
        this.noise = noise;
    }

    public double getDensity() {
        return density;
    }

    public Range getLevel() {
        return level;
    }

    public ProbabilityCollection<T> getLayer() {
        return layer;
    }

    public abstract void place(Chunk chunk, Vector2 coords);
}
