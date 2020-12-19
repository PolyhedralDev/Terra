package com.dfsek.terra.api.world.biome;

import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.world.Flora;
import com.dfsek.terra.api.world.tree.Tree;

public abstract class Decorator {


    public abstract ProbabilityCollection<Tree> getTrees();

    public abstract int getTreeDensity();

    public abstract boolean overrideStructureChance();

    public abstract ProbabilityCollection<Flora> getFlora();

    public abstract int getFloraChance();

}
