package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class SpawnSettingsTemplate implements ObjectTemplate<SpawnSettings> {
    
    private static final Logger logger = LoggerFactory.getLogger(SpawnTypeConfig.class);
    private static boolean used = false;
    @Value("spawns")
    @Default
    private List<SpawnTypeConfig> spawns = null;

    @Value("costs")
    @Default
    private List<SpawnCostConfig> costs = null;

    @Value("probability")
    @Default
    private Float probability = null;

    @Override
    public SpawnSettings get() {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();
        for(SpawnTypeConfig spawn : spawns) {
            SpawnGroup group = spawn.getGroup();
            if (spawn.getEntries() != null) {
                for (SpawnEntry entry : spawn.getEntries()) {
                    builder.spawn(group, entry);
                }
            } else if (spawn.getEntry() != null) {
                if(!used) {
                    logger.warn("The entry sub-field of spawns is deprecated. " +
                                "It is recommended to use the entries sub-field instead ");
                    used = true;
                }
                builder.spawn(group, spawn.getEntry());
            }
        }
        for(SpawnCostConfig cost : costs) {
            builder.spawnCost(cost.getType(), cost.getMass(), cost.getGravity());
        }
        if(probability != null) {
            builder.creatureSpawnProbability(probability);
        }

        return builder.build();
    }
}
