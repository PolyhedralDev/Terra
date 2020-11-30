package com.dfsek.terra.generation.items;

import com.dfsek.terra.procgen.math.Vector2;
import org.bukkit.Chunk;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;

import java.util.Random;

public abstract class PlaceableLayer<T> {
    protected final double density;
    protected final Range level;
    protected final ProbabilityCollection<T> layer;
    protected final FastNoiseLite noise;

    public PlaceableLayer(double density, Range level, ProbabilityCollection<T> layer, FastNoiseLite noise) {
        this.density = density;
        this.level = level;
        this.layer = layer;
        this.noise = noise;
    }

    public FastNoiseLite getNoise() {
        return noise;
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

    public abstract void place(Chunk chunk, Vector2 coords, Random random);
}
