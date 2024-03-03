package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;

import java.util.List;


public class SpawnSettingsTemplate implements ObjectTemplate<SpawnSettings> {
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
            for (SpawnEntry entry : spawn.getEntry()) {
                builder.spawn(group, entry);
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
