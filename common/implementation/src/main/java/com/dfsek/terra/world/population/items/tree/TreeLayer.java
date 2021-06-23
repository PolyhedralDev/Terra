package com.dfsek.terra.world.population.items.tree;

import com.dfsek.terra.api.math.range.ConstantRange;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.world.population.items.PlaceableLayer;

public class TreeLayer extends PlaceableLayer<Tree> {

    public TreeLayer(double density, ConstantRange level, ProbabilityCollectionImpl<Tree> layer, NoiseSampler noise) {
        super(density, level, layer, noise);
    }

    @Override
    public void place(Chunk chunk, Vector2 coords) {
        Tree item = layer.get(noise, coords.getX(), coords.getZ());
        Block current = chunk.getBlock((int) coords.getX(), level.getMax(), (int) coords.getZ());
        for(int ignored : level) {
            current = current.getRelative(BlockFace.DOWN);
            if(item.getSpawnable().contains(current.getType())) {
                item.plant(current.getLocation().add(0, 1, 0), PopulationUtil.getRandom(chunk, coords.hashCode()));
            }
        }
    }
}
