package com.dfsek.terra.generation.items.tree;

import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.gaea.tree.Tree;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.vector.Vector2;
import com.dfsek.terra.generation.items.PlaceableLayer;

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
