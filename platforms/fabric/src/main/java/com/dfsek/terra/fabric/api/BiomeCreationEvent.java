package com.dfsek.terra.fabric.api;

import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.pack.ConfigPack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;

/**
 * Called when a {@link Biome} is created
 * as a delegate to a {@link BiomeBuilder}.
 */
public class BiomeCreationEvent implements PackEvent {
    private final Biome.Builder builder;
    private final BiomeEffects.Builder effectsBuilder;
    private final BiomeBuilder terraBiome;
    private final ConfigPack pack;

    public BiomeCreationEvent(Biome.Builder builder, BiomeEffects.Builder effectsBuilder, BiomeBuilder terraBiome, ConfigPack pack) {
        this.builder = builder;
        this.effectsBuilder = effectsBuilder;
        this.terraBiome = terraBiome;
        this.pack = pack;
    }

    @Override
    public ConfigPack getPack() {
        return pack;
    }

    public BiomeBuilder getTerraBiome() {
        return terraBiome;
    }

    public Biome.Builder getBuilder() {
        return builder;
    }

    public BiomeEffects.Builder getEffectsBuilder() {
        return effectsBuilder;
    }
}
