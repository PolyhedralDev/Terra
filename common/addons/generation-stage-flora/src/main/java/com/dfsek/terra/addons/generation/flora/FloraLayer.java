package com.dfsek.terra.addons.generation.flora;

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
        int cx = (chunk.getX() << 4);
        int cz = (chunk.getZ() << 4);
        Flora item = layer.get(noise, cx + coords.getX(), cz + coords.getZ());
        item.getValidSpawnsAt(chunk, (int) coords.getX(), (int) coords.getZ(), level).forEach(block -> item.plant(block.add(cx, 0, cz), chunk.getWorld()));
    }
}
