package com.dfsek.terra.generation.items.flora;

import com.dfsek.terra.api.math.FastNoiseLite;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.vector.Vector2;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.generation.items.PlaceableLayer;

import java.util.Random;

public class FloraLayer extends PlaceableLayer<Flora> {

    public FloraLayer(double density, Range level, ProbabilityCollection<Flora> layer, FastNoiseLite noise) {
        super(density, level, layer, noise);
    }

    public double getDensity() {
        return density;
    }

    @Override
    public void place(Chunk chunk, Vector2 coords, Random random) {
        Flora item = noise == null ? layer.get(random) : layer.get(noise, (chunk.getX() << 4) + coords.getX(), (chunk.getZ() << 4) + coords.getZ());
        item.getValidSpawnsAt(chunk, (int) coords.getX(), (int) coords.getZ(), level).forEach(block -> item.plant(block.getLocation()));
    }
}
