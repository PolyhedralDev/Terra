package com.dfsek.terra.world.population.items.tree;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;

import java.util.Random;

public class TerraTree implements Tree {
    private final MaterialSet spawnable;
    private final int yOffset;
    private final ProbabilityCollection<StructureScript> structure;

    public TerraTree(MaterialSet spawnable, int yOffset, ProbabilityCollection<StructureScript> structure) {
        this.spawnable = spawnable;
        this.yOffset = yOffset;
        this.structure = structure;
    }

    @Override
    public synchronized boolean plant(Location location, Random random) {
        return structure.get(random).executeDirect(location.clone().add(0, yOffset, 0), random, Rotation.fromDegrees(90 * random.nextInt(4)));
    }

    @Override
    public MaterialSet getSpawnable() {
        return spawnable;
    }
}
