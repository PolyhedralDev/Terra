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
        Flora item = layer.get(noise, (chunk.getX() << 4) + coords.getX(), (chunk.getZ() << 4) + coords.getZ());
        item.getValidSpawnsAt(chunk, (int) coords.getX(), (int) coords.getZ(), level).forEach(block -> item.plant(block.toLocation(chunk.getWorld())));
    }
}
