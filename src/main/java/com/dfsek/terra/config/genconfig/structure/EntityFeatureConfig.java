package com.dfsek.terra.config.genconfig.structure;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.structure.features.EntityFeature;
import com.dfsek.terra.structure.features.Feature;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.EntityType;
import org.polydev.gaea.math.Range;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityFeatureConfig implements FeatureConfig {
    private final EntityFeature feature;

    @SuppressWarnings("unchecked")
    public EntityFeatureConfig(Map<String, Object> items) throws InvalidConfigurationException {
        if(! items.containsKey("entity")) throw new ConfigException("No EntityType specified!", "EntityFeature");
        if(! items.containsKey("amount")) throw new ConfigException("No amount specified!", "EntityFeature");
        if(! items.containsKey("attempts")) throw new ConfigException("Attempts not specified!", "EntityFeature");
        if(! items.containsKey("in-height"))
            throw new ConfigException("Spawn Checking Height not specified!", "EntityFeature");
        if(! items.containsKey("spawnable-on"))
            throw new ConfigException("No Spawnable-on materials specified!", "EntityFeature");
        if(! items.containsKey("spawnable-in"))
            throw new ConfigException("No Spawnable-in materials specified!", "EntityFeature");

        EntityType type;
        try {
            type = EntityType.valueOf((String) items.get("entity"));
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("No such EntityType: " + items.get("entity"));
        } catch(ClassCastException e) {
            throw new InvalidConfigurationException("Error in Entity Configuration!");
        }

        int attempts = (Integer) items.get("attempts");
        int height = (Integer) items.get("in-height");

        Range amount;
        try {
            Map<String, Integer> amountMap = (Map<String, Integer>) items.get("amount");
            amount = new Range(amountMap.get("min"), amountMap.get("max"));
        } catch(ClassCastException e) {
            throw new InvalidConfigurationException("Error in Amount Configuration!");
        }

        Set<Material> on = ConfigUtil.toBlockData((List<String>) items.get("spawnable-on"), "SpawnableOn", "");
        Set<Material> in = ConfigUtil.toBlockData((List<String>) items.get("spawnable-in"), "SpawnableIn", "");

        this.feature = new EntityFeature(type, amount, attempts, on, in, height);
        Debug.info("Loaded EntityFeature with type: " + type);
    }

    @Override
    public Feature getFeature() {
        return feature;
    }
}
