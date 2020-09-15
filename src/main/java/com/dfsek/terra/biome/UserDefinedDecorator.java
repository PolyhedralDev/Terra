package com.dfsek.terra.biome;

import com.dfsek.terra.TerraTree;
import com.dfsek.terra.config.BiomeConfig;
import com.dfsek.terra.config.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Fauna;
import org.polydev.gaea.world.FaunaType;

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
                try {
                    Bukkit.getLogger().info("[Terra] Adding " + e.getKey() + " to biome's fauna list with weight " + e.getValue());
                    fauna.add(FaunaType.valueOf(e.getKey()), (Integer) e.getValue());
                } catch(IllegalArgumentException ex) {
                    try {
                        Bukkit.getLogger().info("[Terra] Is custom fauna: true");
                        Fauna faunaCustom = ConfigUtil.getFauna(e.getKey());
                        fauna.add(faunaCustom, (Integer) e.getValue());
                    } catch(NullPointerException ex2) {
                        throw new IllegalArgumentException("SEVERE configuration error for fauna in biome " + config.getFriendlyName() + ", ID " +  config.getBiomeID() + "\n\nFauna with ID " + e.getKey() + " cannot be found!");
                    }
                }
            }
        }
        if(config.contains("trees")) {
            for(Map.Entry<String, Object> e : config.getConfigurationSection("trees").getValues(false).entrySet()) {
                if(e.getKey().startsWith("TERRA:")) {
                    trees.add(TerraTree.valueOf(e.getKey().substring(6)), (Integer) e.getValue());
                } else {
                    trees.add(TreeType.valueOf(e.getKey()), (Integer) e.getValue());
                }
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
