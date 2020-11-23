package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.tree.Tree;

import java.util.Map;
import java.util.Objects;

public class BiomeTreeConfig extends TerraConfigSection {
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();
    private final Map<Tree, Range> treeHeights = new Object2ObjectOpenHashMap<>();
    private int treeDensity;

    public BiomeTreeConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        ConfigurationSection c = parent.getConfigurationSection("trees.items");
        if(c == null) return;
        Map<String, Object> cfg = c.getValues(false);
        if(cfg.size() == 0) return;
        treeDensity = parent.getInt("trees.density", 0);

        for(Map.Entry<String, Object> e : cfg.entrySet()) {
            try {
                Map<?, ?> val = ((ConfigurationSection) e.getValue()).getValues(false);
                Map<?, ?> y = ((ConfigurationSection) val.get("y")).getValues(false);
                Tree tree;
                try {
                    tree = Objects.requireNonNull(parent.getConfig().getTreeRegistry().get(e.getKey()));
                } catch(NullPointerException ex2) {
                    throw new NotFoundException("Tree", e.getKey(), parent.getID());
                }
                trees.add(tree, (Integer) val.get("weight"));
                treeHeights.put(tree, new Range((Integer) y.get("min"), (Integer) y.get("max")));
            } catch(ClassCastException ex) {
                throw new ConfigException("Unable to parse Tree configuration! Check YAML syntax.", parent.getID());
            }
        }
    }

    public ProbabilityCollection<Tree> getTrees() {
        return trees;
    }

    public Map<Tree, Range> getTreeHeights() {
        return treeHeights;
    }

    public int getTreeDensity() {
        return treeDensity;
    }
}
