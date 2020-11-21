package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.Range;

import java.util.List;
import java.util.Map;

public class BiomeSnowConfig extends TerraConfigSection {
    private final int[] snowHeights;
    private boolean doSnow = false;

    public BiomeSnowConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        snowHeights = new int[256];
        List<Map<?, ?>> maps = parent.getYaml().getMapList("snow");
        if(maps.size() == 0) return;
        try {
            for(Map<?, ?> e : maps) {
                Range r = new Range((Integer) e.get("min"), (Integer) e.get("max"));
                int chance = (Integer) e.get("chance");
                for(int y : r) {
                    if(chance > 0) doSnow = true;
                    snowHeights[y] = chance;
                }
            }
        } catch(ClassCastException e) {
            if(ConfigUtil.debug) e.printStackTrace();
            throw new ConfigException("Unable to parse Snow configuration! Check YAML syntax.", parent.getID());
        }
    }

    public int getSnowChance(int y) {
        return snowHeights[y];
    }

    public boolean doSnow() {
        return doSnow;
    }
}
