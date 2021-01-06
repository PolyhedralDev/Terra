package com.dfsek.terra.population.items.tree;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.FastNoiseLite;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.population.items.PlaceableLayer;

import java.util.Random;

public class TreeLayer extends PlaceableLayer<Tree> {

    public TreeLayer(double density, Range level, ProbabilityCollection<Tree> layer, FastNoiseLite noise) {
        super(density, level, layer, noise);
    }

    @Override
    public void place(Chunk chunk, Vector2 coords, Random random) {
        Tree item = layer.get(random);
        Block current = chunk.getBlock((int) coords.getX(), level.getMax(), (int) coords.getZ());
        for(int ignored : level) {
            current = current.getRelative(BlockFace.DOWN);
            if(item.getSpawnable().contains(current.getType())) {
                item.plant(current.getLocation().add(0, 1, 0), random);
            }
        }
    }
}
