package com.dfsek.terra.world.population.items.flora;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.Flora;
import com.dfsek.terra.world.population.items.PlaceableLayer;

public class FloraLayer extends PlaceableLayer<Flora> {

    public FloraLayer(double density, Range level, ProbabilityCollection<Flora> layer, NoiseSampler noise) {
        super(density, level, layer, noise);
    }

    public double getDensity() {
        return density;
    }

    @Override
    public void place(Chunk chunk, Vector2 coords) {
        int cx = chunk.getX() << 4;
        int cz = chunk.getZ() << 4;
        Flora item = layer.get(noise, coords.getX() + cx, coords.getZ() + cz);
        item.getValidSpawnsAt(chunk, (int) coords.getX(), (int) coords.getZ(), level).forEach(block -> item.plant(block.toLocation(chunk.getWorld()).add(cx, 0, cz)));
    }
}
