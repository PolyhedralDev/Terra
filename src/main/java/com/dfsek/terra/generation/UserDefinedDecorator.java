package com.dfsek.terra.generation;

import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Flora;

public class UserDefinedDecorator extends Decorator {

    private final ProbabilityCollection<Flora> flora;
    private final ProbabilityCollection<Tree> trees;
    private final int floraChance;
    private final int treeDensity;

    public UserDefinedDecorator(ProbabilityCollection<Flora> flora, ProbabilityCollection<Tree> trees, int floraChance, int treeDensity) {
        this.flora = flora;
        this.trees = trees;

        this.floraChance = floraChance;
        this.treeDensity = treeDensity;
    }

    @Override
    public ProbabilityCollection<Tree> getTrees() {
        return trees;
    }

    @Override
    public int getTreeDensity() {
        return treeDensity;
    }

    @Override
    public boolean overrideStructureChance() {
        return false;
    }

    @Override
    public Biome getVanillaBiome() {
        return null;
    }

    @Override
    public ProbabilityCollection<Flora> getFlora() {
        return flora;
    }

    @Override
    public int getFloraChance() {
        return floraChance;
    }
}
