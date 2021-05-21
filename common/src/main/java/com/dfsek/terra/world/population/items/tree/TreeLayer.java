package com.dfsek.terra.world.population.items.tree;

import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.util.world.PopulationUtil;
import com.dfsek.terra.world.population.items.PlaceableLayer;

public class TreeLayer extends PlaceableLayer<Tree> {

    public TreeLayer(double density, Range level, ProbabilityCollection<Tree> layer, NoiseSampler noise) {
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
