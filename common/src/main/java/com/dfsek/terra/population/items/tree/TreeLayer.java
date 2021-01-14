package com.dfsek.terra.population.items.tree;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.population.items.PlaceableLayer;
import com.dfsek.terra.util.PopulationUtil;

public class TreeLayer extends PlaceableLayer<Tree> {

    public TreeLayer(double density, Range level, ProbabilityCollection<Tree> layer, NoiseSampler noise, TerraPlugin main) {
        super(density, level, layer, noise, main);
    }

    @Override
    public void place(Chunk chunk, Vector2 coords) {
        Tree item = layer.get(noise, coords.getX(), coords.getZ());
        TerraWorld terraWorld = main.getWorld(chunk.getWorld());
        int cx = chunk.getX() << 4;
        int cz = chunk.getZ() << 4;
        BlockData current;
        for(int y : level) {
            current = terraWorld.getUngeneratedBlock(coords.getBlockX() + cx, y, coords.getBlockZ() + cz);
            if(item.getSpawnable().contains(current.getMaterial())) {
                item.plant(coords.toVector3(y).add(cx, 1, cz).toLocation(chunk.getWorld()), PopulationUtil.getRandom(chunk));
            }
        }
    }
}
