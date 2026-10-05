package com.dfsek.terra.bukkit.nms.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dfsek.terra.bukkit.nms.NMSVersionBindings;


public class SpawnSettingsTemplate implements ObjectTemplate<MobSpawnSettings> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpawnSettingsTemplate.class);
    private static final AtomicBoolean PROBABILITY_WARNING_LOGGED = new AtomicBoolean();

    private final NMSVersionBindings bindings;

    @Value("spawns")
    @Default
    private List<SpawnTypeConfig> spawns = List.of();

    @Value("costs")
    @Default
    private List<SpawnCostConfig> costs = List.of();

    @Value("probability")
    @Default
    private Float probability = null;

    public SpawnSettingsTemplate(NMSVersionBindings bindings) {
        this.bindings = bindings;
    }

    @Override
    public MobSpawnSettings get() {
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
        for(SpawnTypeConfig spawn : spawns) {
            MobCategory category = spawn.getGroup();
            for(SpawnEntryConfig entry : spawn.getEntries()) {
                bindings.addSpawn(builder, category, entry.getType(), entry.getWeight(), entry.getMinCount(), entry.getMaxCount());
            }
        }
        for(SpawnCostConfig cost : costs) {
            bindings.addSpawnCost(builder, cost.getType(), cost.getMass(), cost.getGravity());
        }
        if(probability != null && !bindings.setCreatureGenerationProbability(builder, probability) &&
           PROBABILITY_WARNING_LOGGED.compareAndSet(false, true)) {
            LOGGER.warn("This server version ignores biome creature-generation probability values.");
        }

        return builder.build();
    }
}
