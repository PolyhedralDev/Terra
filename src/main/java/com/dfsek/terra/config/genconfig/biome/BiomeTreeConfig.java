package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Flora;

import java.util.HashMap;
import java.util.Map;

public class BiomeTreeConfig extends TerraConfigSection {
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();
    private final Map<Tree, Range> treeHeights = new HashMap<>();
    public BiomeTreeConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        ConfigurationSection c = parent.getConfigurationSection("trees");
        if(c == null) return;
        Map<String, Object> cfg = c.getValues(false);
        if(cfg.size() == 0) return;
        for(Map.Entry<String, Object> e : cfg.entrySet()) {
            try {
                Map<?, ?> val = ((ConfigurationSection) e.getValue()).getValues(false);
                Map<?, ?> y = ((ConfigurationSection) val.get("y")).getValues(false);
                Tree tree = TreeType.valueOf(e.getKey());
                trees.add(tree, (Integer) val.get("weight"));
                treeHeights.put(tree, new Range((Integer) y.get("min"), (Integer) y.get("max")));
            } catch(ClassCastException ex) {
                throw new ConfigException("Unable to parse Tree configuration! Check YAML syntax.", parent.getID());
            } catch(IllegalArgumentException ex) {
                throw new ConfigException("Invalid tree type: \"" + e.getKey() + "\"", parent.getID());
            }
        }
    }

    public ProbabilityCollection<Tree> getTrees() {
        return trees;
    }

    public Map<Tree, Range> getTreeHeights() {
        return treeHeights;
    }
}
