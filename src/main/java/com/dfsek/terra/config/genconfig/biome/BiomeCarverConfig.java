package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.config.genconfig.CarverConfig;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class BiomeCarverConfig extends TerraConfigSection {
    private final Map<CarverConfig, Integer> carvers = new HashMap<>();
    @SuppressWarnings("unchecked")
    public BiomeCarverConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        List<Map<?, ?>> cfg = parent.getMapList("carving");
        if(cfg.size() == 0) return;
        for(Map<?, ?> e : cfg) {
            for(Map.Entry<?, ?> entry : e.entrySet()) {
                try {
                    CarverConfig c = parent.getConfig().getCarver((String) entry.getKey());
                    if(c == null) throw new NotFoundException("Carver", (String) entry.getKey(), parent.getID());
                    Debug.info("Got carver " + c + ". Adding with weight " + entry.getValue());
                    carvers.put(c, (Integer) entry.getValue());
                } catch(ClassCastException ex) {
                    throw new ConfigException("Unable to parse Carver configuration! Check YAML syntax.", parent.getID());
                } catch(NullPointerException ex) {
                    throw new NotFoundException("carver", (String) entry.getKey(), parent.getID());
                }
            }
        }
    }

    public Map<CarverConfig, Integer> getCarvers() {
        return carvers;
    }
}
