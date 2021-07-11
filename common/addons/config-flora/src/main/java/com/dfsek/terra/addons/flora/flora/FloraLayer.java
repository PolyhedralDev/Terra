package com.dfsek.terra.addons.flora.flora;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.Flora;

public class FloraLayer {
    protected final double density;
    protected final Range level;
    protected final ProbabilityCollection<Flora> layer;
    protected final NoiseSampler noise;

    public FloraLayer(double density, Range level, ProbabilityCollection<Flora> layer, NoiseSampler noise) {
        this.density = density;
        this.level = level;
        this.layer = layer;
        this.noise = noise;
    }

    public NoiseSampler getNoise() {
        return noise;
    }

    public double getDensity() {
        return density;
    }

    public Range getLevel() {
        return level;
    }

    public ProbabilityCollection<Flora> getLayer() {
        return layer;
    }

    public void place(Chunk chunk, Vector2 coords) {
        Flora item = layer.get(noise, (chunk.getX() << 4) + coords.getX(), (chunk.getZ() << 4) + coords.getZ());
        item.getValidSpawnsAt(chunk, (int) coords.getX(), (int) coords.getZ(), level).forEach(block -> item.plant(block, chunk.getWorld()));
    }
}
