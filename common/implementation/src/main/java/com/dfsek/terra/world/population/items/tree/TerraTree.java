package com.dfsek.terra.world.population.items.tree;

import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.world.Tree;

import java.util.Random;

public class TerraTree implements Tree {
    private final MaterialSet spawnable;
    private final int yOffset;
    private final ProbabilityCollection<Structure> structure;

    public TerraTree(MaterialSet spawnable, int yOffset, ProbabilityCollection<Structure> structure) {
        this.spawnable = spawnable;
        this.yOffset = yOffset;
        this.structure = structure;
    }

    @Override
    public synchronized boolean plant(Location location, Random random) {
        return structure.get(random).generateDirect(location.clone().add(0, yOffset, 0), random, Rotation.fromDegrees(90 * random.nextInt(4)));
    }

    @Override
    public MaterialSet getSpawnable() {
        return spawnable;
    }
}
