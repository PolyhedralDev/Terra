package com.dfsek.terra.biome;

import com.dfsek.terra.config.BiomeConfig;
import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Fauna;

import java.util.Map;

public class UserDefinedDecorator extends Decorator {

    private final ProbabilityCollection<Fauna> fauna = new ProbabilityCollection<>();
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();
    private final int faunaChance;
    private final int treeChance;
    private final int treeDensity;

    public UserDefinedDecorator(BiomeConfig config) {
        if(config.contains("fauna")) {
            for(Map.Entry<String, Object> e : config.getConfigurationSection("fauna").getValues(false).entrySet()) {
                fauna.add(Fauna.valueOf(e.getKey()), (Integer) e.getValue());
            }
        }
        if(config.contains("trees")) {
            for(Map.Entry<String, Object> e : config.getConfigurationSection("trees").getValues(false).entrySet()) {
                trees.add(Tree.valueOf(e.getKey()), (Integer) e.getValue());
            }
        }
        faunaChance = config.getInt("fauna-chance", 0);
        treeChance = config.getInt("tree-chance", 0);
        treeDensity = config.getInt("tree-density", 0);
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
