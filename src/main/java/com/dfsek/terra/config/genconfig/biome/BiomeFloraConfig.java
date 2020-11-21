package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.world.Flora;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BiomeFloraConfig extends TerraConfigSection {
    private final ProbabilityCollection<Flora> floras = new ProbabilityCollection<>();
    private final Map<Flora, Range> floraHeights = new HashMap<>();
    private int floraAttempts;
    private int floraChance;
    private boolean floraSimplex;
    private FastNoiseLite floraNoise;

    public BiomeFloraConfig(TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        ConfigurationSection cfg = parent.getYaml().getConfigurationSection("flora.items");
        if(cfg == null) return;
        floraSimplex = parent.getYaml().getBoolean("flora.simplex.enable", false);
        floraAttempts = parent.getYaml().getInt("flora.attempts", 1);
        floraChance = parent.getYaml().getInt("flora.chance", 0);
        double floraFreq = parent.getYaml().getDouble("flora.simplex.frequency", 0.1);
        int floraSeed = parent.getYaml().getInt("flora.simplex.seed", 2403);
        if(floraSimplex) {
            floraNoise = new FastNoiseLite(floraSeed);
            floraNoise.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            floraNoise.setFrequency(floraFreq);
        }

        for(Map.Entry<String, Object> e : cfg.getValues(false).entrySet()) {
            try {
                Map<?, ?> val = ((ConfigurationSection) e.getValue()).getValues(false);
                Map<?, ?> y = ((ConfigurationSection) val.get("y")).getValues(false);
                Flora flora;
                try {
                    flora = Objects.requireNonNull(parent.getConfig().getFloraRegistry().get(e.getKey()));
                } catch(NullPointerException ex) {
                    throw new NotFoundException("Flora", e.getKey(), parent.getID());
                }
                floras.add(flora, (Integer) val.get("weight"));
                floraHeights.put(flora, new Range((Integer) y.get("min"), (Integer) y.get("max")));
            } catch(ClassCastException ex) {
                Debug.stack(ex);
                throw new ConfigException("Unable to parse Flora configuration! Check YAML syntax.", parent.getID());
            }
        }

    }

    public ProbabilityCollection<Flora> getFlora() {
        return floras;
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
