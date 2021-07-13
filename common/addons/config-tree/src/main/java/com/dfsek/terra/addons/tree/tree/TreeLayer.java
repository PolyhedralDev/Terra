package com.dfsek.terra.addons.tree.tree;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.Tree;

public class TreeLayer {
    protected final double density;
    protected final Range level;
    protected final ProbabilityCollection<Tree> layer;
    protected final NoiseSampler noise;

    public TreeLayer(double density, Range level, ProbabilityCollection<Tree> layer, NoiseSampler noise) {
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

    public ProbabilityCollection<Tree> getLayer() {
        return layer;
    }

    public void place(Chunk chunk, Vector2 coords) {
        Tree item = layer.get(noise, coords.getX(), coords.getZ());
        BlockState current;
        int i = 0;
        for(int ignored : level) {
            current = chunk.getBlock((int) coords.getX(), level.getMax() - i, (int) coords.getZ());
            if(item.getSpawnable().contains(current.getBlockType())) {
                item.plant(new Vector3((int) coords.getX(), level.getMax() - i, (int) coords.getZ()), chunk.getWorld(), PopulationUtil.getRandom(chunk, coords.hashCode()));
            }
            i--;
        }
    }
}
