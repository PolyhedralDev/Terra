package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.config.genconfig.OreConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.Range;

import java.util.HashMap;
import java.util.Map;

public class BiomeOreConfig extends TerraConfigSection {
    private final Map<OreConfig, Range> ores = new HashMap<>();
    private final Map<OreConfig, Range> oreHeights = new HashMap<>();
    public BiomeOreConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        ConfigurationSection c = parent.getConfigurationSection("ores");
        if(c == null) return;
        Map<String, Object> cfg = c.getValues(false);
        try {
            for(Map.Entry<String, Object> m : cfg.entrySet()) {
                OreConfig ore = parent.getConfig().getOre(m.getKey());
                if(ore == null) throw new NotFoundException("Ore", m.getKey(), parent.getID());
                ores.put(ore, new Range(((ConfigurationSection) m.getValue()).getInt("min"), ((ConfigurationSection)  m.getValue()).getInt("max")));
                oreHeights.put(ore, new Range(((ConfigurationSection) m.getValue()).getInt("min-height"), ((ConfigurationSection)  m.getValue()).getInt("max-height")));
            }
        } catch(ClassCastException e) {
            if(ConfigUtil.debug) e.printStackTrace();
            throw new ConfigException("Unable to parse Flora configuration! Check YAML syntax.", parent.getID());
        }
    }

    public Map<OreConfig, Range> getOres() {
        return ores;
    }

    public Map<OreConfig, Range> getOreHeights() {
        return oreHeights;
    }
}
