package com.dfsek.terra.addons.attribute.minecraft;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.RawValue;


public final class MinecraftAttributeTypes {
    private final AttributeType<MoonPhase> moonPhase;
    private final AttributeType<BedRule> bedRule;
    private final AttributeType<RawValue> particle;
    private final AttributeType<List<AmbientParticle>> ambientParticles;
    private final AttributeType<BackgroundMusic> backgroundMusic;
    private final AttributeType<AmbientSounds> ambientSounds;
    private final AttributeType<MobSpawnSettings> mobSpawnSettings;
    private final AttributeType<RegistryKey> identifier;
    private final AttributeType<Either<RegistryKey, TagKey>> identifierOrTag;
    private final AttributeType<IntProviderValue> intProvider;

    public MinecraftAttributeTypes(AttributeHandle handle) {
        MinecraftCodecs codecs = new MinecraftCodecs(handle);
        this.moonPhase = handle.typeBuilder("moon_phase", codecs.moonPhase()).build();
        this.bedRule = handle.typeBuilder("bed_rule", codecs.bedRule()).build();
        this.particle = handle.typeBuilder("particle", codecs.particle()).build();
        this.ambientParticles = handle.typeBuilder("ambient_particles", codecs.ambientParticles())
            .op(Operation.APPEND, codecs.ambientParticles(), MinecraftAttributeTypes::append)
            .composable(Operation.APPEND, MinecraftAttributeTypes::append)
            .build();
        this.backgroundMusic = handle.typeBuilder("background_music", codecs.backgroundMusic()).build();
        this.ambientSounds = handle.typeBuilder("ambient_sounds", codecs.ambientSounds()).build();
        this.mobSpawnSettings = handle.typeBuilder("mob_spawn_settings", codecs.mobSpawnSettings())
            .op(Operation.OVERLAY, codecs.mobSpawnSettings(), MinecraftAttributeTypes::overlay)
            .composable(Operation.OVERLAY, MinecraftAttributeTypes::overlay)
            .build();
        this.identifier = handle.typeBuilder("identifier", codecs.identifier()).build();
        this.identifierOrTag = handle.typeBuilder("identifier_or_tag", codecs.identifierOrTag()).build();
        this.intProvider = handle.typeBuilder("int_provider", codecs.intProvider()).build();
    }

    public AttributeType<MoonPhase> moonPhase() {
        return moonPhase;
    }

    public AttributeType<BedRule> bedRule() {
        return bedRule;
    }

    public AttributeType<RawValue> particle() {
        return particle;
    }

    public AttributeType<List<AmbientParticle>> ambientParticles() {
        return ambientParticles;
    }

    public AttributeType<BackgroundMusic> backgroundMusic() {
        return backgroundMusic;
    }

    public AttributeType<AmbientSounds> ambientSounds() {
        return ambientSounds;
    }

    public AttributeType<MobSpawnSettings> mobSpawnSettings() {
        return mobSpawnSettings;
    }

    public AttributeType<RegistryKey> identifier() {
        return identifier;
    }

    public AttributeType<Either<RegistryKey, TagKey>> identifierOrTag() {
        return identifierOrTag;
    }

    public AttributeType<IntProviderValue> intProvider() {
        return intProvider;
    }

    private static List<AmbientParticle> append(List<AmbientParticle> s, List<AmbientParticle> a) {
        if(a.isEmpty()) return s;
        if(s.isEmpty()) return a;
        List<AmbientParticle> out = new ArrayList<>(s.size() + a.size());
        out.addAll(s);
        out.addAll(a);
        return List.copyOf(out);
    }

    private static MobSpawnSettings overlay(MobSpawnSettings first, MobSpawnSettings second) {
        if(first.spawnsByCategory().isEmpty() && first.spawnCosts().isEmpty()) return second;
        if(second.spawnsByCategory().isEmpty() && second.spawnCosts().isEmpty()) return first;
        if(second.spawnsByCategory().keySet().containsAll(first.spawnsByCategory().keySet())
           && second.spawnCosts().keySet().containsAll(first.spawnCosts().keySet())) {
            return second;
        }
        Map<String, List<MobSpawnSettings.Spawner>> categories = new LinkedHashMap<>(first.spawnsByCategory());
        categories.putAll(second.spawnsByCategory());
        Map<RegistryKey, MobSpawnSettings.Cost> costs = new LinkedHashMap<>(first.spawnCosts());
        costs.putAll(second.spawnCosts());
        return new MobSpawnSettings(categories, costs);
    }
}
