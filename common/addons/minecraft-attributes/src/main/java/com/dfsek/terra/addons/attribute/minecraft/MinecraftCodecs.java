package com.dfsek.terra.addons.attribute.minecraft;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.TreeSet;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeCodec;
import com.dfsek.terra.api.world.attribute.AttributeCodecs;
import com.dfsek.terra.api.world.attribute.AttributeCodecs.ObjectReader;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.RawValue;


public final class MinecraftCodecs {
    public static final String NAMESPACE = "minecraft";

    // Same wording as the core identifier codec.
    private static final String EXPECTED_IDENTIFIER = "expected an identifier like \"" + NAMESPACE + ":id\"";

    private static final Set<String> MOB_CATEGORIES = Set.of("monster", "creature", "ambient", "axolotls", "underground_water_creature",
        "water_creature", "water_ambient", "misc");

    private final AttributeCodecs codecs;
    private final DecodeContext plain;
    private final AttributeCodec<String> idString;
    private final AttributeCodec<RegistryKey> identifier;
    private final AttributeCodec<Either<RegistryKey, TagKey>> identifierOrTag;
    private final AttributeCodec<MoonPhase> moonPhase;
    private final AttributeCodec<RawValue> particle;
    private final AttributeCodec<AmbientParticle> ambientParticle;
    private final AttributeCodec<List<AmbientParticle>> ambientParticles;
    private final AttributeCodec<IntProviderValue> intProvider;
    private final AttributeCodec<SoundRef> soundRef;
    private final AttributeCodec<Music> music;
    private final AttributeCodec<BackgroundMusic> backgroundMusic;
    private final AttributeCodec<AmbientSounds.Mood> mood;
    private final AttributeCodec<List<AmbientSounds.Additions>> additions;
    private final AttributeCodec<AmbientSounds> ambientSounds;
    private final AttributeCodec<BedRule.Rule> bedRuleRule;
    private final AttributeCodec<BedRule> bedRule;
    private final AttributeCodec<List<MobSpawnSettings.Spawner>> spawners;
    private final AttributeCodec<MobSpawnSettings.Cost> spawnCost;
    private final AttributeCodec<Map<String, List<MobSpawnSettings.Spawner>>> spawnsByCategory;
    private final AttributeCodec<Map<RegistryKey, MobSpawnSettings.Cost>> spawnCosts;
    private final AttributeCodec<MobSpawnSettings> mobSpawnSettings;

    public MinecraftCodecs(AttributeHandle handle) {
        AttributeCodecs c = handle.codecs();
        this.codecs = c;
        this.plain = handle.plainContext();
        this.idString = c.string(EXPECTED_IDENTIFIER);

        AttributeCodec<RegistryKey> coreIdentifier = c.identifier(NAMESPACE);
        this.identifier = c.of((raw, context) -> {
            String s = idString.decode(raw, context);
            return lowerCase(coreIdentifier.decode(s, plain), s);
        }, coreIdentifier::encode);

        AttributeCodec<Either<RegistryKey, TagKey>> coreIdentifierOrTag = c.identifierOrTag(NAMESPACE);
        this.identifierOrTag = c.of((raw, context) -> {
            String s = idString.decode(raw, context);
            Either<RegistryKey, TagKey> id = coreIdentifierOrTag.decode(s, plain);
            lowerCase(id.hasLeft() ? id.getLeft().orElseThrow() : id.getRight().orElseThrow().key(), s);
            return id;
        }, coreIdentifierOrTag::encode);

        this.moonPhase = c.enumCodec("moon phase", MoonPhase.class);

        this.particle = c.of((raw, context) -> {
            Object value = context.resolve(raw);
            if(!(value instanceof Map<?, ?> map) || !map.containsKey("type")) {
                throw new DecodeException("expected a particle object with a \"type\" key, got " + c.describe(value));
            }
            if(map.get("type") instanceof String type) {
                try {
                    identifier.decode(type, plain);
                } catch(DecodeException e) {
                    throw new DecodeException("\"type\" of particle: " + e.getMessage(), e);
                }
            }
            return c.raw().decode(value, context);
        }, RawValue::value);

        this.ambientParticle = c.of((raw, context) -> {
            ObjectReader f = c.reader("ambient particle", raw, context);
            RawValue value = f.required("particle", this.particle);
            float probability = f.required("probability", c.unitFloat());
            f.finish();
            return new AmbientParticle(value, probability);
        }, value -> c.writer()
            .put("particle", particle, value.particle())
            .put("probability", c.unitFloat(), value.probability())
            .map());

        this.ambientParticles = c.list(ambientParticle);

        this.intProvider = c.of((raw, context) -> {
            String expected = "expected an integer or an int provider object";
            Object value = context.resolve(raw);
            if(value instanceof Map<?, ?> map) {
                if(!map.containsKey("type")) throw new DecodeException("int provider object is missing \"type\"");
                return new IntProviderValue(OptionalInt.empty(), Optional.of(c.raw().decode(value, context)));
            }
            if(value instanceof Number) return new IntProviderValue(OptionalInt.of(c.intCodec().decode(value, context)), Optional.empty());
            if(value instanceof String) {
                try {
                    return new IntProviderValue(OptionalInt.of(c.intCodec().decode(value, context)), Optional.empty());
                } catch(DecodeException e) {
                    throw new DecodeException(expected + ", got " + c.describe(value), e);
                }
            }
            throw new DecodeException(expected + ", got " + c.describe(value));
        }, value -> value.constant().isPresent() ? (Object) value.constant().getAsInt() : value.provider().orElseThrow().value());

        this.soundRef = c.of((raw, context) -> {
            Object value = context.resolve(raw);
            if(value instanceof String) return new SoundRef(identifier.decode(value, context), OptionalDouble.empty());
            if(value instanceof Map<?, ?>) {
                ObjectReader f = c.reader("sound", value, context);
                RegistryKey id = f.required("sound_id", identifier);
                Optional<Double> range = f.optional("range", c.doubleCodec());
                f.finish();
                return new SoundRef(id, range.map(OptionalDouble::of).orElse(OptionalDouble.empty()));
            }
            throw new DecodeException("expected a sound ID or a {sound_id, range} object, got " + c.describe(value));
        }, value -> {
            if(value.range().isEmpty()) return identifier.encode(value.id());
            return c.writer()
                .put("sound_id", identifier, value.id())
                .put("range", c.doubleCodec(), value.range().getAsDouble())
                .map();
        });

        this.music = c.of((raw, context) -> {
            ObjectReader f = c.reader("music", raw, context);
            SoundRef sound = f.required("sound", soundRef);
            int min = f.required("min_delay", c.nonNegativeInt());
            int max = f.required("max_delay", c.nonNegativeInt());
            boolean replace = f.orDefault("replace_current_music", c.booleanCodec(), false);
            f.finish();
            return new Music(sound, min, max, replace);
        }, value -> c.writer()
            .put("sound", soundRef, value.sound())
            .put("min_delay", c.nonNegativeInt(), value.minDelay())
            .put("max_delay", c.nonNegativeInt(), value.maxDelay())
            .put("replace_current_music", c.booleanCodec(), value.replaceCurrentMusic())
            .map());

        this.backgroundMusic = c.of((raw, context) -> {
            ObjectReader f = c.reader("background music", raw, context);
            Optional<Music> def = f.optional("default", music);
            Optional<Music> creative = f.optional("creative", music);
            Optional<Music> underwater = f.optional("underwater", music);
            f.finish();
            return new BackgroundMusic(def, creative, underwater);
        }, value -> c.writer()
            .optional("default", music, value.defaultMusic())
            .optional("creative", music, value.creative())
            .optional("underwater", music, value.underwater())
            .map());

        this.mood = c.of((raw, context) -> {
            ObjectReader f = c.reader("ambient mood", raw, context);
            SoundRef sound = f.required("sound", soundRef);
            int tickDelay = f.required("tick_delay", c.intCodec());
            int extent = f.required("block_search_extent", c.intCodec());
            double offset = f.required("offset", c.doubleCodec());
            f.finish();
            return new AmbientSounds.Mood(sound, tickDelay, extent, offset);
        }, value -> c.writer()
            .put("sound", soundRef, value.sound())
            .put("tick_delay", c.intCodec(), value.tickDelay())
            .put("block_search_extent", c.intCodec(), value.blockSearchExtent())
            .put("offset", c.doubleCodec(), value.offset())
            .map());

        this.additions = c.compactList(c.of((raw, context) -> {
            ObjectReader f = c.reader("ambient additions", raw, context);
            SoundRef sound = f.required("sound", soundRef);
            double chance = f.required("tick_chance", c.doubleCodec());
            f.finish();
            return new AmbientSounds.Additions(sound, chance);
        }, value -> c.writer()
            .put("sound", soundRef, value.sound())
            .put("tick_chance", c.doubleCodec(), value.tickChance())
            .map()));

        this.ambientSounds = c.of((raw, context) -> {
            ObjectReader f = c.reader("ambient sounds", raw, context);
            Optional<SoundRef> loop = f.optional("loop", soundRef);
            Optional<AmbientSounds.Mood> moodValue = f.optional("mood", mood);
            List<AmbientSounds.Additions> additionsValue = f.orDefault("additions", additions, List.of());
            f.finish();
            return new AmbientSounds(loop, moodValue, additionsValue);
        }, value -> c.writer()
            .optional("loop", soundRef, value.loop())
            .optional("mood", mood, value.mood())
            .put("additions", additions, value.additions())
            .map());

        this.bedRuleRule = c.enumCodec("bed rule", BedRule.Rule.class);

        this.bedRule = c.of((raw, context) -> {
            ObjectReader f = c.reader("bed rule", raw, context);
            BedRule.Rule canSleep = f.required("can_sleep", bedRuleRule);
            BedRule.Rule canSetSpawn = f.required("can_set_spawn", bedRuleRule);
            boolean onUse = f.orDefault("destroy_on_use", c.booleanCodec(), false);
            boolean onLeave = f.orDefault("destroy_on_leave", c.booleanCodec(), false);
            Optional<RawValue> message = f.optional("error_message", c.raw());
            f.finish();
            return new BedRule(canSleep, canSetSpawn, onUse, onLeave, message);
        }, value -> c.writer()
            .put("can_sleep", bedRuleRule, value.canSleep())
            .put("can_set_spawn", bedRuleRule, value.canSetSpawn())
            .put("destroy_on_use", c.booleanCodec(), value.destroyOnUse())
            .put("destroy_on_leave", c.booleanCodec(), value.destroyOnLeave())
            .optional("error_message", c.raw(), value.errorMessage())
            .map());

        this.spawners = c.list(c.of((raw, context) -> {
            ObjectReader f = c.reader("mob spawner", raw, context);
            RegistryKey type = f.required("type", identifier);
            int weight = f.required("weight", c.nonNegativeInt());
            IntProviderValue count = f.required("count", intProvider);
            f.finish();
            return new MobSpawnSettings.Spawner(type, weight, count);
        }, value -> c.writer()
            .put("type", identifier, value.type())
            .put("weight", c.nonNegativeInt(), value.weight())
            .put("count", intProvider, value.count())
            .map()));

        this.spawnCost = c.of((raw, context) -> {
            ObjectReader f = c.reader("spawn cost", raw, context);
            double budget = f.required("energy_budget", c.doubleCodec());
            double charge = f.required("charge", c.doubleCodec());
            f.finish();
            return new MobSpawnSettings.Cost(budget, charge);
        }, value -> c.writer()
            .put("energy_budget", c.doubleCodec(), value.energyBudget())
            .put("charge", c.doubleCodec(), value.charge())
            .map());

        this.spawnsByCategory = c.of((raw, context) -> {
            ObjectReader categories = c.reader("spawns_by_category", raw, context);
            Map<String, List<MobSpawnSettings.Spawner>> out = new LinkedHashMap<>();
            for(String key : categories.keys()) {
                if(!MOB_CATEGORIES.contains(key)) {
                    throw new DecodeException("unknown mob category \"" + key + "\", expected one of " + new TreeSet<>(MOB_CATEGORIES));
                }
                out.put(key, categories.required(key, spawners));
            }
            categories.finish();
            return out;
        }, value -> {
            Map<String, Object> out = new LinkedHashMap<>();
            value.forEach((category, list) -> out.put(category, spawners.encode(list)));
            return out;
        });

        // Map keys are never preprocessed.
        this.spawnCosts = c.of((raw, context) -> {
            ObjectReader costs = c.reader("spawn_costs", raw, context);
            Map<RegistryKey, MobSpawnSettings.Cost> out = new LinkedHashMap<>();
            for(String key : costs.keys()) {
                out.put(identifier.decode(key, plain), costs.required(key, spawnCost));
            }
            costs.finish();
            return out;
        }, value -> {
            Map<String, Object> out = new LinkedHashMap<>();
            value.forEach((entity, cost) -> out.put((String) identifier.encode(entity), spawnCost.encode(cost)));
            return out;
        });

        this.mobSpawnSettings = c.of((raw, context) -> {
            ObjectReader f = c.reader("mob spawn settings", raw, context);
            Map<String, List<MobSpawnSettings.Spawner>> byCategory = f.required("spawns_by_category", spawnsByCategory);
            Map<RegistryKey, MobSpawnSettings.Cost> costs = f.required("spawn_costs", spawnCosts);
            f.finish();
            return new MobSpawnSettings(byCategory, costs);
        }, value -> c.writer()
            .put("spawns_by_category", spawnsByCategory, value.spawnsByCategory())
            .put("spawn_costs", spawnCosts, value.spawnCosts())
            .map());
    }

    private RegistryKey lowerCase(RegistryKey key, String written) throws DecodeException {
        try {
            return MinecraftIds.requireLowerCase(key, written);
        } catch(IllegalArgumentException e) {
            throw new DecodeException(EXPECTED_IDENTIFIER + ", got " + codecs.describe(written), e);
        }
    }

    public AttributeCodec<RegistryKey> identifier() {
        return identifier;
    }

    public AttributeCodec<Either<RegistryKey, TagKey>> identifierOrTag() {
        return identifierOrTag;
    }

    public AttributeCodec<MoonPhase> moonPhase() {
        return moonPhase;
    }

    public AttributeCodec<RawValue> particle() {
        return particle;
    }

    public AttributeCodec<List<AmbientParticle>> ambientParticles() {
        return ambientParticles;
    }

    public AttributeCodec<IntProviderValue> intProvider() {
        return intProvider;
    }

    public AttributeCodec<SoundRef> soundRef() {
        return soundRef;
    }

    public AttributeCodec<BackgroundMusic> backgroundMusic() {
        return backgroundMusic;
    }

    public AttributeCodec<AmbientSounds> ambientSounds() {
        return ambientSounds;
    }

    public AttributeCodec<BedRule> bedRule() {
        return bedRule;
    }

    public AttributeCodec<MobSpawnSettings> mobSpawnSettings() {
        return mobSpawnSettings;
    }
}
