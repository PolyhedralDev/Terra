package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;

import java.util.Map;

public class BiomeTreeConfig extends TerraConfigSection {
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();
    public BiomeTreeConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        ConfigurationSection c = parent.getConfigurationSection("trees");
        if(c == null) return;
        Map<String, Object> cfg = c.getValues(false);
        if(cfg.size() == 0) return;
        for(Map.Entry<String, Object> e : cfg.entrySet()) {
            try {
                trees.add(TreeType.valueOf(e.getKey()), (Integer) e.getValue());
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
}
