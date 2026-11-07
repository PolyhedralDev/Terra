package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;


public class SpawnSettingsTemplate implements ObjectTemplate<MobSpawnSettings> {
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
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
        for(SpawnTypeConfig spawn : spawns) {
            MobCategory group = spawn.getGroup();
            for(SpawnEntryConfig entry : spawn.getEntries()) {
                MobSpawnSettings.SpawnerData spawnEntry = entry.getSpawnEntry();
                builder.addSpawn(spawnEntry.type(), group, entry.getWeight(), spawnEntry.count());
            }
        }
        for(SpawnCostConfig cost : costs) {
            builder.addMobSpawnCost(cost.getType(), cost.getMass(), cost.getGravity());
        }
        // TODO: environment attributes. Creature generation probability is now the
        //  gameplay/creature_world_gen_spawn_probability biome attribute, so it is ignored here.

        return builder.build();
    }
}
