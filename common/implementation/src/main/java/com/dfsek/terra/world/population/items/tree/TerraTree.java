package com.dfsek.terra.world.population.items.tree;

import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;

import java.util.Random;

public class TerraTree implements Tree {
    private final MaterialSet spawnable;
    private final int yOffset;
    private final ProbabilityCollectionImpl<StructureScript> structure;

    public TerraTree(MaterialSet spawnable, int yOffset, ProbabilityCollectionImpl<StructureScript> structure) {
        this.spawnable = spawnable;
        this.yOffset = yOffset;
        this.structure = structure;
    }

    @Override
    public synchronized boolean plant(LocationImpl location, Random random) {
        return structure.get(random).executeDirect(location.clone().add(0, yOffset, 0), random, Rotation.fromDegrees(90 * random.nextInt(4)));
    }

    @Override
    public MaterialSet getSpawnable() {
        return spawnable;
    }
}
