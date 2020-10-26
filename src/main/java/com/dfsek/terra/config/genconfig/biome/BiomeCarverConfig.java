package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.config.genconfig.CarverConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.HashMap;
import java.util.Map;

public class BiomeCarverConfig extends TerraConfigSection {
    private final Map<CarverConfig, Integer> carvers = new HashMap<>();

    public BiomeCarverConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        ConfigurationSection configurationSection = parent.getConfigurationSection("carving");

        Map<String, Object> cfg;
        if(configurationSection != null) {
            cfg = configurationSection.getValues(false);
        } else return;
        if(cfg.size() == 0) return;
        for(Map.Entry<String, Object> entry : cfg.entrySet()) {
            try {
                CarverConfig c = parent.getConfig().getCarver(entry.getKey());
                if(c == null) throw new NotFoundException("Carver", entry.getKey(), parent.getID());
                Debug.info("Got carver " + c + ". Adding with weight " + entry.getValue());
                carvers.put(c, (Integer) entry.getValue());
            } catch(ClassCastException ex) {
                throw new ConfigException("Unable to parse Carver configuration! Check YAML syntax.", parent.getID());
            } catch(NullPointerException ex) {
                throw new NotFoundException("carver", entry.getKey(), parent.getID());
            }
        }
    }

    public Map<CarverConfig, Integer> getCarvers() {
        return carvers;
    }
}
