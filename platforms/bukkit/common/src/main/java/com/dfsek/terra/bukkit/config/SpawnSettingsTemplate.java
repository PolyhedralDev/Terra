package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import dev.wyck.biome.entity.MobCategory;
import dev.wyck.biome.entity.MobSpawnSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class SpawnSettingsTemplate implements ObjectTemplate<MobSpawnSettings> {

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
    public MobSpawnSettings get() {
        MobSpawnSettings.Builder builder = MobSpawnSettings.builder();
        for(SpawnTypeConfig spawn : spawns) {
            MobCategory group = spawn.getGroup();
            for(SpawnEntryConfig entry : spawn.getEntries()) {
                builder.addSpawn(group, entry.getWeight(), entry.getSpawnEntry());
            }
        }
        for(SpawnCostConfig cost : costs) {
            builder.addMobSpawnCost(cost.getType(), cost.getMass(), cost.getGravity());
        }
        if(probability != null) {
            builder.creatureGenerationProbability(probability);
        }

        return builder.build();
    }
}
