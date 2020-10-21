package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.FloraType;

import java.util.HashMap;
import java.util.Map;

public class BiomeFloraConfig extends TerraConfigSection {
    private final ProbabilityCollection<Flora> flora = new ProbabilityCollection<>();
    private final Map<Flora, Range> floraHeights = new HashMap<>();
    private int floraAttempts;
    private int floraChance;
    private boolean floraSimplex;
    private FastNoiseLite floraNoise;
    public BiomeFloraConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        ConfigurationSection cfg = parent.getConfigurationSection("flora.items");
        if(cfg == null) return;
        floraSimplex = parent.getBoolean("flora.simplex.enable", false);
        floraAttempts = parent.getInt("flora.attempts", 1);
        floraChance = parent.getInt("flora.chance", 0);
        float floraFreq = (float) parent.getDouble("flora.simplex.frequency", 0.1);
        int floraSeed = parent.getInt("flora.simplex.seed", 2403);
        if(floraSimplex) {
            floraNoise = new FastNoiseLite(floraSeed);
            floraNoise.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            floraNoise.setFrequency(floraFreq);
        }

        try {
            for(Map.Entry<String, Object> e : cfg.getValues(false).entrySet()) {
                Map<?, ?> val = ((ConfigurationSection) e.getValue()).getValues(false);
                Map<?, ?> y = ((ConfigurationSection) val.get("y")).getValues(false);
                try {
                    Debug.info("Adding " + e.getKey() + " to biome's flora list with weight " + e.getValue());
                    Flora floraObj = FloraType.valueOf(e.getKey());
                    flora.add(floraObj, (Integer) val.get("weight"));
                    floraHeights.put(floraObj, new Range((Integer) y.get("min"), (Integer) y.get("max")));
                } catch(IllegalArgumentException ex) {
                    try {
                        Debug.info("[Terra] Is custom flora: true");
                        Flora floraCustom = parent.getConfig().getFlora(e.getKey());
                        if(floraCustom == null) throw new NotFoundException("Flora", e.getKey(), parent.getID());
                        flora.add(floraCustom, (Integer) val.get("weight"));
                        floraHeights.put(floraCustom, new Range((Integer) y.get("min"), (Integer) y.get("max")));
                    } catch(NullPointerException ex2) {
                        throw new NotFoundException("Flora", e.getKey(), parent.getID());
                    }
                }
            }
        } catch(ClassCastException e) {
            if(ConfigUtil.debug) e.printStackTrace();
            throw new ConfigException("Unable to parse Flora configuration! Check YAML syntax.", parent.getID());
        }
    }

    public ProbabilityCollection<Flora> getFlora() {
        return flora;
    }

    public Map<Flora, Range> getFloraHeights() {
        return floraHeights;
    }

    public FastNoiseLite getFloraNoise() {
        return floraNoise;
    }

    public int getFloraAttempts() {
        return floraAttempts;
    }

    public boolean isFloraSimplex() {
        return floraSimplex;
    }

    public int getFloraChance() {
        return floraChance;
    }
}
