package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import dev.wyck.biome.entity.MobCategory;
import dev.wyck.biome.entity.MobSpawnSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class SpawnSettingsTemplate implements ObjectTemplate<MobSpawnSettings> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpawnSettingsTemplate.class);
    private static final AtomicBoolean PROBABILITY_WARNING_LOGGED = new AtomicBoolean(false);

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
        if(spawns != null) {
            for(SpawnTypeConfig spawn : spawns) {
                MobCategory group = spawn.getGroup();
                for(SpawnEntryConfig entry : spawn.getEntries()) {
                    builder.addSpawn(entry.getType(), group, entry.getWeight(), entry.getCount());
                }
            }
        }
        if(costs != null) {
            for(SpawnCostConfig cost : costs) {
                builder.addMobSpawnCost(cost.getType(), cost.getMass(), cost.getGravity());
            }
        }
        if(probability != null && PROBABILITY_WARNING_LOGGED.compareAndSet(false, true)) {
            LOGGER.warn("Biome spawning.probability is ignored on this server version.");
        }

        return builder.build();
    }
}
