package com.dfsek.terra.biome;

import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Fauna;

public class UserDefinedDecorator extends Decorator {

    private final ProbabilityCollection<Fauna> fauna;
    private final ProbabilityCollection<Tree> trees;
    private final int faunaChance;
    private final int treeChance;
    private final int treeDensity;

    public UserDefinedDecorator(ProbabilityCollection<Fauna> fauna, ProbabilityCollection<Tree> trees, int faunaChance, int treeChance, int treeDensity) {
        this.fauna = fauna;
        this.trees = trees;

        this.faunaChance = faunaChance;
        this.treeChance = treeChance;
        this.treeDensity = treeDensity;
    }

    @Override
    public ProbabilityCollection<Tree> getTrees() {
        return trees;
    }

    public int getTreeChance() {
        return treeChance;
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
    public boolean shouldGenerateSnow() {
        return false;
    }

    @Override
    public Biome getVanillaBiome() {
        return null;
    }

    @Override
    public ProbabilityCollection<Fauna> getFauna() {
        return fauna;
    }

    @Override
    public int getFaunaChance() {
        return faunaChance;
    }
}
