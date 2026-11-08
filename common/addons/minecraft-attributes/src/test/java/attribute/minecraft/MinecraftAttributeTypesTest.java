package attribute.minecraft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.addons.attribute.minecraft.AmbientParticle;
import com.dfsek.terra.addons.attribute.minecraft.AmbientSounds;
import com.dfsek.terra.addons.attribute.minecraft.BackgroundMusic;
import com.dfsek.terra.addons.attribute.minecraft.BedRule;
import com.dfsek.terra.addons.attribute.minecraft.IntProviderValue;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributeTypes;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftCodecs;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftIds;
import com.dfsek.terra.addons.attribute.minecraft.MobSpawnSettings;
import com.dfsek.terra.addons.attribute.minecraft.MoonPhase;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.RawValue;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.*;


class MinecraftAttributeTypesTest {
    private static final float EPS = 1e-6f;
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final MinecraftAttributeTypes TYPES = new MinecraftAttributeTypes(HANDLE);
    private static final DecodeContext PLAIN = HANDLE.plainContext();
    private static final MinecraftCodecs CODECS = new MinecraftCodecs(HANDLE);

    private static <T> T mod(AttributeType<T> type, Operation op, T subject, Object rawArg) throws DecodeException {
        return type.apply(op, subject, type.decodeArgument(op, rawArg, PLAIN));
    }

    private static Map<String, Object> map(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for(int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }

    @Test
    void everyTypeSupportsOverride() {
        List<AttributeType<?>> all = List.of(TYPES.moonPhase(), TYPES.bedRule(), TYPES.particle(), TYPES.ambientParticles(),
            TYPES.backgroundMusic(), TYPES.ambientSounds(), TYPES.mobSpawnSettings(), TYPES.identifier(), TYPES.identifierOrTag(),
            TYPES.intProvider());
        for(AttributeType<?> t : all) assertTrue(t.operations().contains(Operation.OVERRIDE), t.id());
    }

    @Test
    void operationSets() {
        assertEquals(EnumSet.of(Operation.OVERRIDE), TYPES.identifier().operations());
        assertEquals(EnumSet.of(Operation.OVERRIDE), TYPES.identifierOrTag().operations());
        assertEquals(EnumSet.of(Operation.OVERRIDE, Operation.APPEND), TYPES.ambientParticles().operations());
        assertEquals(EnumSet.of(Operation.OVERRIDE, Operation.OVERLAY), TYPES.mobSpawnSettings().operations());
    }

    @Test
    void idsAreVanillaTypeIds() {
        assertEquals("moon_phase", TYPES.moonPhase().id());
        assertEquals("bed_rule", TYPES.bedRule().id());
        assertEquals("particle", TYPES.particle().id());
        assertEquals("ambient_particles", TYPES.ambientParticles().id());
        assertEquals("background_music", TYPES.backgroundMusic().id());
        assertEquals("ambient_sounds", TYPES.ambientSounds().id());
        assertEquals("mob_spawn_settings", TYPES.mobSpawnSettings().id());
        assertEquals("identifier", TYPES.identifier().id());
        assertEquals("identifier_or_tag", TYPES.identifierOrTag().id());
        assertEquals("int_provider", TYPES.intProvider().id());
    }

    @Test
    void decodeFailuresReadTheSameEverywhere() {
        String bedRule = assertThrows(DecodeException.class,
            () -> TYPES.bedRule().decode(map("can_sleep", "sometimes", "can_set_spawn", "always"), PLAIN)).getMessage();
        assertEquals("\"can_sleep\" of bed rule: unknown bed rule \"sometimes\", expected one of [always, when_dark, never]", bedRule);
        String moonPhase = assertThrows(DecodeException.class, () -> TYPES.moonPhase().decode("x", PLAIN)).getMessage();
        assertTrue(moonPhase.startsWith("unknown moon phase \"x\", expected one of [full_moon, "), moonPhase);
    }

    @Test
    void enumsAreStringIdentifiable() {
        assertEquals("waxing_gibbous", MoonPhase.WAXING_GIBBOUS.getID());
        assertEquals("when_dark", BedRule.Rule.WHEN_DARK.getID());
        assertEquals("new_moon", TYPES.moonPhase().encode(MoonPhase.NEW_MOON));
    }

    @Test
    void ambientParticlesAppend() throws DecodeException {
        AttributeType<List<AmbientParticle>> t = TYPES.ambientParticles();
        List<AmbientParticle> one = t.decode(List.of(map("particle", map("type", "minecraft:crimson_spore"), "probability", 0.025)), PLAIN);
        List<AmbientParticle> two = t.decode(List.of(map("particle", map("type", "minecraft:white_ash"), "probability", 0.1)), PLAIN);
        assertEquals(1, one.size());
        assertEquals(0.025f, one.get(0).probability(), EPS);
        assertEquals(new RawValue(map("type", "minecraft:crimson_spore")), one.get(0).particle());
        assertEquals(one, t.apply(Operation.APPEND, one, List.of()));
        assertEquals(two, t.apply(Operation.APPEND, List.of(), two));
        assertEquals(List.of(one.get(0), two.get(0)), t.apply(Operation.APPEND, one, two));
        assertEquals(List.of(one.get(0), two.get(0)),
            mod(t, Operation.APPEND, one, List.of(map("particle", map("type", "minecraft:white_ash"), "probability", 0.1))));
    }

    @Test
    void ambientParticleValidation() {
        AttributeType<List<AmbientParticle>> t = TYPES.ambientParticles();
        assertThrows(DecodeException.class, () -> t.decode(List.of(map("particle", map("type", "x"), "probability", 1.5)), PLAIN));
        assertThrows(DecodeException.class, () -> t.decode(List.of(map("particle", map("type", "x"))), PLAIN));
        assertThrows(DecodeException.class, () -> t.decode(List.of(map("particle", "x", "probability", 0.5)), PLAIN));
        assertThrows(DecodeException.class,
            () -> t.decode(List.of(map("particle", map("type", "x"), "probability", 0.5, "extra", 1)), PLAIN));
        assertThrows(DecodeException.class, () -> t.decode(map("particle", map("type", "x"), "probability", 0.5), PLAIN));
    }

    private static MobSpawnSettings spawns(Map<String, Object> raw) throws DecodeException {
        return TYPES.mobSpawnSettings().decode(raw, PLAIN);
    }

    private static Map<String, Object> spawner(String type, int weight, int count) {
        return map("type", type, "weight", weight, "count", count);
    }

    @Test
    void mobSpawnDecode() throws DecodeException {
        MobSpawnSettings s = spawns(map(
            "spawns_by_category", map("ambient", List.of(spawner("minecraft:bat", 10, 8))),
            "spawn_costs", map("minecraft:enderman", map("energy_budget", 0.7, "charge", 0.15))));
        MobSpawnSettings.Spawner sp = s.spawnsByCategory().get("ambient").get(0);
        assertEquals(RegistryKey.parse("minecraft:bat"), sp.type());
        assertEquals(10, sp.weight());
        assertEquals(8, sp.count().constant().getAsInt());
        assertEquals(0.7, s.spawnCosts().get(RegistryKey.parse("minecraft:enderman")).energyBudget(), 1e-9);
        assertEquals(0.15, s.spawnCosts().get(RegistryKey.parse("minecraft:enderman")).charge(), 1e-9);
        assertThrows(DecodeException.class, () -> spawns(map("spawns_by_category", map("bogus", List.of()), "spawn_costs", map())));
        assertThrows(DecodeException.class, () -> spawns(map("spawns_by_category", map())));
        assertThrows(DecodeException.class, () -> spawns(map("spawns_by_category", map(), "spawn_costs", map(), "x", 1)));
    }

    @Test
    void mobSpawnCountMapIsKeptRaw() throws DecodeException {
        MobSpawnSettings s = spawns(map("spawns_by_category", map("monster", List.of(
                map("type", "minecraft:zombie", "weight", 5,
                    "count", map("type", "minecraft:uniform", "min_inclusive", 1, "max_inclusive", 3)))),
            "spawn_costs", map()));
        IntProviderValue c = s.spawnsByCategory().get("monster").get(0).count();
        assertTrue(c.constant().isEmpty());
        assertTrue(c.provider().isPresent());
    }

    @Test
    void mobSpawnOverlayRules() throws DecodeException {
        AttributeType<MobSpawnSettings> t = TYPES.mobSpawnSettings();
        MobSpawnSettings empty = MobSpawnSettings.EMPTY;
        MobSpawnSettings a = spawns(map("spawns_by_category", map("monster", List.of(spawner("minecraft:zombie", 1, 1))),
            "spawn_costs", map()));
        assertSame(a, t.apply(Operation.OVERLAY, empty, a));
        assertSame(a, t.apply(Operation.OVERLAY, a, empty));
        MobSpawnSettings sup = spawns(map("spawns_by_category", map("monster", List.of(spawner("minecraft:husk", 1, 1)),
            "creature", List.of(spawner("minecraft:pig", 1, 1))), "spawn_costs", map()));
        assertSame(sup, t.apply(Operation.OVERLAY, a, sup));
        MobSpawnSettings s = spawns(map("spawns_by_category", map("monster", List.of(spawner("minecraft:zombie", 1, 1)),
                "creature", List.of(spawner("minecraft:cow", 1, 1))),
            "spawn_costs", map("minecraft:a", map("energy_budget", 1, "charge", 1))));
        MobSpawnSettings arg = spawns(map("spawns_by_category", map("ambient", List.of(spawner("minecraft:bat", 1, 1)),
                "creature", List.of(spawner("minecraft:pig", 1, 1))),
            "spawn_costs", map("minecraft:b", map("energy_budget", 2, "charge", 2))));
        MobSpawnSettings r = t.apply(Operation.OVERLAY, s, arg);
        assertEquals(RegistryKey.parse("minecraft:zombie"), r.spawnsByCategory().get("monster").get(0).type());
        assertEquals(RegistryKey.parse("minecraft:pig"), r.spawnsByCategory().get("creature").get(0).type());
        assertEquals(RegistryKey.parse("minecraft:bat"), r.spawnsByCategory().get("ambient").get(0).type());
        assertEquals(Set.of(RegistryKey.parse("minecraft:a"), RegistryKey.parse("minecraft:b")), r.spawnCosts().keySet());
    }

    @Test
    void mobSpawnOverlayEmptyCategoryClears() throws DecodeException {
        AttributeType<MobSpawnSettings> t = TYPES.mobSpawnSettings();
        MobSpawnSettings s = spawns(map("spawns_by_category", map("monster", List.of(spawner("minecraft:zombie", 1, 1)),
            "creature", List.of(spawner("minecraft:cow", 1, 1))), "spawn_costs", map()));
        MobSpawnSettings arg = spawns(map("spawns_by_category", map("monster", List.of()), "spawn_costs", map()));
        MobSpawnSettings r = mod(t, Operation.OVERLAY, s, map("spawns_by_category", map("monster", List.of()), "spawn_costs", map()));
        assertTrue(r.spawnsByCategory().get("monster").isEmpty());
        assertEquals(1, r.spawnsByCategory().get("creature").size());
        assertEquals(r, t.apply(Operation.OVERLAY, s, arg));
    }

    @Test
    void ambientSoundsDecode() throws DecodeException {
        AmbientSounds a = TYPES.ambientSounds().decode(map(
            "additions", map("sound", "minecraft:ambient.crimson_forest.additions", "tick_chance", 0.0111),
            "loop", "minecraft:ambient.crimson_forest.loop",
            "mood", map("block_search_extent", 8, "offset", 2.0, "sound", "minecraft:ambient.crimson_forest.mood", "tick_delay", 6000)),
            PLAIN);
        assertEquals(1, a.additions().size());
        assertEquals(0.0111, a.additions().get(0).tickChance(), 1e-9);
        assertEquals(RegistryKey.parse("minecraft:ambient.crimson_forest.loop"), a.loop().orElseThrow().id());
        assertEquals(6000, a.mood().orElseThrow().tickDelay());
        assertEquals(8, a.mood().orElseThrow().blockSearchExtent());
        assertEquals(2.0, a.mood().orElseThrow().offset(), 1e-9);

        AmbientSounds list = TYPES.ambientSounds().decode(map(
            "additions", List.of(map("sound", "a:b", "tick_chance", 0.1), map("sound", "a:c", "tick_chance", 0.2))), PLAIN);
        assertEquals(2, list.additions().size());
        assertTrue(list.loop().isEmpty());
        assertEquals(AmbientSounds.EMPTY, TYPES.ambientSounds().decode(map(), PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.ambientSounds().decode(map("looop", "a:b"), PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.ambientSounds().decode(map("mood", map("sound", "a:b")), PLAIN));
    }

    @Test
    void soundRefForms() throws DecodeException {
        AmbientSounds a = TYPES.ambientSounds().decode(map("loop", map("sound_id", "a:b", "range", 16)), PLAIN);
        assertEquals(RegistryKey.parse("a:b"), a.loop().orElseThrow().id());
        assertEquals(16.0, a.loop().orElseThrow().range().getAsDouble(), 1e-9);
        AmbientSounds b = TYPES.ambientSounds().decode(map("loop", map("sound_id", "a:b")), PLAIN);
        assertTrue(b.loop().orElseThrow().range().isEmpty());
        assertEquals(RegistryKey.parse("minecraft:ambient.cave"),
            TYPES.ambientSounds().decode(map("loop", "ambient.cave"), PLAIN).loop().orElseThrow().id());
        assertThrows(DecodeException.class, () -> TYPES.ambientSounds().decode(map("loop", 5), PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.ambientSounds().decode(map("loop", "Not Valid"), PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.ambientSounds().decode(map("loop", map("range", 1)), PLAIN));
    }

    @Test
    void upperCaseIdRejected() {
        DecodeException e = assertThrows(DecodeException.class, () -> CODECS.soundRef().decode("minecraft:Ambient.Cave", PLAIN));
        assertEquals("expected an identifier like \"minecraft:id\", got \"minecraft:Ambient.Cave\"", e.getMessage());
        assertEquals(path("minecraft:Ambient.Cave"), e.getCause().getMessage());
        assertEquals(namespace("Minecraft:x"), lowerCaseFailure(() -> CODECS.soundRef().decode("Minecraft:x", PLAIN)));
    }

    private static String path(String written) {
        return "invalid ID \"" + written + "\": the path must be non-empty and use only a-z, 0-9, _, ., / and -";
    }

    private static String namespace(String written) {
        return "invalid ID \"" + written + "\": the namespace must be non-empty and use only a-z, 0-9, _, . and -";
    }

    private static String lowerCaseFailure(Executable decode) {
        Throwable t = assertThrows(DecodeException.class, decode);
        while(t != null && !(t instanceof IllegalArgumentException)) t = t.getCause();
        assertNotNull(t, "no lower-case rule failure in the cause chain");
        return t.getMessage();
    }

    @Test
    void lowerCaseIds() {
        assertEquals(path("minecraft:Music.Game"),
            lowerCaseFailure(() -> TYPES.ambientSounds().decode(Map.of("loop", "minecraft:Music.Game"), PLAIN)));
        assertEquals(path("Music.Game"), lowerCaseFailure(() -> TYPES.backgroundMusic().decode(
            map("default", map("sound", "Music.Game", "min_delay", 1, "max_delay", 2)), PLAIN)));
        assertEquals(path("minecraft:Flame"), lowerCaseFailure(() -> TYPES.particle().decode(map("type", "minecraft:Flame"), PLAIN)));
        assertEquals(path("Idle"), lowerCaseFailure(() -> TYPES.identifier().decode("Idle", PLAIN)));
        assertEquals(path("#minecraft:Logs"), lowerCaseFailure(() -> TYPES.identifierOrTag().decode("#minecraft:Logs", PLAIN)));
        assertEquals(path("#Logs"), lowerCaseFailure(() -> TYPES.identifierOrTag().decode("#Logs", PLAIN)));
        assertEquals(namespace("Minecraft:stone"), lowerCaseFailure(() -> TYPES.identifierOrTag().decode("Minecraft:stone", PLAIN)));
        assertEquals(path("minecraft:Zombie"), lowerCaseFailure(() -> spawns(map(
            "spawns_by_category", map("monster", List.of(spawner("minecraft:Zombie", 1, 1))), "spawn_costs", map()))));
        assertEquals(namespace("Minecraft:enderman"), lowerCaseFailure(() -> spawns(map("spawns_by_category", map(),
            "spawn_costs", map("Minecraft:enderman", map("energy_budget", 1, "charge", 1))))));
    }

    @Test
    void requireLowerCase() {
        RegistryKey key = RegistryKey.parse("minecraft:visual/fog_color");
        assertSame(key, MinecraftIds.requireLowerCase(key));
        assertSame(key, MinecraftIds.requireLowerCase(key, "visual/fog_color"));
        assertEquals(namespace("Mod:thing"),
            assertThrows(IllegalArgumentException.class, () -> MinecraftIds.requireLowerCase(RegistryKey.parse("Mod:thing"))).getMessage());
        assertEquals(path("mod:Thing"),
            assertThrows(IllegalArgumentException.class, () -> MinecraftIds.requireLowerCase(RegistryKey.parse("mod:Thing"))).getMessage());
        assertEquals(path("Thing"), assertThrows(IllegalArgumentException.class,
            () -> MinecraftIds.requireLowerCase(RegistryKey.parse("minecraft:Thing"), "Thing")).getMessage());
    }

    @Test
    void integersFollowTheCoreRule() {
        String weight = assertThrows(DecodeException.class, () -> spawns(map("spawns_by_category",
            map("monster", List.of(map("type", "minecraft:zombie", "weight", 1.5, "count", 1))), "spawn_costs", map()))).getMessage();
        assertTrue(weight.endsWith("\"weight\" of mob spawner: expected an integer, got 1.5"), weight);
        assertThrows(DecodeException.class, () -> TYPES.intProvider().decode(99999999999L, PLAIN));
    }

    @Test
    void backgroundMusicDecode() throws DecodeException {
        BackgroundMusic m = TYPES.backgroundMusic().decode(map(
            "default", map("max_delay", 24000, "min_delay", 12000, "sound", "minecraft:music.game", "replace_current_music", true),
            "underwater", map("max_delay", 24000, "min_delay", 12000, "sound", "minecraft:music.under_water")), PLAIN);
        assertTrue(m.defaultMusic().orElseThrow().replaceCurrentMusic());
        assertFalse(m.underwater().orElseThrow().replaceCurrentMusic());
        assertEquals(12000, m.underwater().orElseThrow().minDelay());
        assertEquals(24000, m.underwater().orElseThrow().maxDelay());
        assertTrue(m.creative().isEmpty());
        assertEquals(BackgroundMusic.EMPTY, TYPES.backgroundMusic().decode(map(), PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.backgroundMusic().decode(map("bogus", 1), PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.backgroundMusic().decode(map("default", map("sound", "a:b", "min_delay", 1)), PLAIN));
        assertThrows(DecodeException.class,
            () -> TYPES.backgroundMusic().decode(map("default", map("sound", "a:b", "min_delay", -1, "max_delay", 1)), PLAIN));
    }

    @Test
    void bedRuleDecode() throws DecodeException {
        BedRule r = TYPES.bedRule().decode(map("can_set_spawn", "never", "can_sleep", "when_dark", "destroy_on_leave", true,
            "error_message", map("translate", "block.minecraft.bed.no_sleep")), PLAIN);
        assertEquals(BedRule.Rule.WHEN_DARK, r.canSleep());
        assertEquals(BedRule.Rule.NEVER, r.canSetSpawn());
        assertFalse(r.destroyOnUse());
        assertTrue(r.destroyOnLeave());
        assertTrue(r.errorMessage().isPresent());
        BedRule plain = TYPES.bedRule().decode(map("can_set_spawn", "always", "can_sleep", "always"), PLAIN);
        assertTrue(plain.errorMessage().isEmpty());
        assertThrows(DecodeException.class, () -> TYPES.bedRule().decode(map("can_sleep", "always"), PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.bedRule().decode(map("can_sleep", "sometimes", "can_set_spawn", "always"), PLAIN));
        assertThrows(DecodeException.class,
            () -> TYPES.bedRule().decode(map("can_sleep", "always", "can_set_spawn", "always", "nope", 1), PLAIN));
    }

    @Test
    void simpleTypesDecode() throws DecodeException {
        assertEquals(MoonPhase.NEW_MOON, TYPES.moonPhase().decode("new_moon", PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.moonPhase().decode("blue_moon", PLAIN));
        assertEquals(RegistryKey.parse("minecraft:idle"), TYPES.identifier().decode("minecraft:idle", PLAIN));
        assertEquals(RegistryKey.parse("minecraft:idle"), TYPES.identifier().decode("idle", PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.identifier().decode("Not Valid", PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.identifier().decode(3, PLAIN));
        assertEquals("expected an identifier, not a tag, got \"#minecraft:logs\"",
            assertThrows(DecodeException.class, () -> TYPES.identifier().decode("#minecraft:logs", PLAIN)).getMessage());
        assertEquals(Either.left(RegistryKey.parse("minecraft:stone")), TYPES.identifierOrTag().decode("stone", PLAIN));
        assertEquals(Either.right(TagKey.parse("#minecraft:logs")), TYPES.identifierOrTag().decode("#logs", PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.identifierOrTag().decode("#", PLAIN));
    }

    @Test
    void particleDecode() throws DecodeException {
        RawValue p = TYPES.particle().decode(map("type", "minecraft:dripping_dripstone_lava"), PLAIN);
        assertEquals(new RawValue(map("type", "minecraft:dripping_dripstone_lava")), p);
        assertThrows(DecodeException.class, () -> TYPES.particle().decode(map("color", 1), PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.particle().decode("minecraft:flame", PLAIN));
    }

    @Test
    void intProviderDecode() throws DecodeException {
        IntProviderValue c = TYPES.intProvider().decode(7, PLAIN);
        assertEquals(7, c.constant().getAsInt());
        assertTrue(c.provider().isEmpty());
        IntProviderValue p = TYPES.intProvider().decode(map("type", "minecraft:uniform", "min_inclusive", 1, "max_inclusive", 3), PLAIN);
        assertTrue(p.constant().isEmpty());
        assertTrue(p.provider().isPresent());
        assertThrows(DecodeException.class, () -> TYPES.intProvider().decode("x", PLAIN));
        assertThrows(DecodeException.class, () -> TYPES.intProvider().decode(1.5, PLAIN));
    }

    @Test
    void overrideArgumentDecodesWithValueDecoder() throws DecodeException {
        assertEquals(MoonPhase.NEW_MOON, mod(TYPES.moonPhase(), Operation.OVERRIDE, MoonPhase.FULL_MOON, "new_moon"));
    }

    @Test
    void nestedDecodeContext() throws DecodeException {
        List<Object> seen = new ArrayList<>();
        DecodeContext recording = new DecodeContext() {
            @Override
            public <V> V load(Type type, Object raw) throws DecodeException {
                seen.add(raw);
                return PLAIN.load(type, raw);
            }

            @Override
            public Object resolve(Object raw) {
                return raw;
            }
        };
        Map<String, Object> music = map("sound", "minecraft:music.game", "min_delay", 1, "max_delay", 2);
        TYPES.backgroundMusic().decode(map("default", music), recording);
        assertTrue(seen.contains("minecraft:music.game"), seen.toString());
        assertTrue(seen.contains(1), seen.toString());
        assertTrue(seen.contains(2), seen.toString());
        assertTrue(seen.contains(music), seen.toString());
    }

    private static DecodeContext referencing(Map<String, Object> targets) {
        return new DecodeContext() {
            @Override
            public <V> V load(Type type, Object raw) throws DecodeException {
                return PLAIN.load(type, raw);
            }

            @Override
            public Object resolve(Object raw) {
                if(raw instanceof String s && targets.containsKey(s)) return resolve(targets.get(s));
                if(raw instanceof Map<?, ?> m) {
                    Map<String, Object> out = new LinkedHashMap<>();
                    m.forEach((k, v) -> out.put((String) k, resolve(v)));
                    return out;
                }
                if(raw instanceof List<?> l) return l.stream().map(this::resolve).toList();
                return raw;
            }
        };
    }

    @Test
    void resolveBeforeShape() throws DecodeException {
        DecodeContext context = referencing(map(
            "@sound", map("sound_id", "minecraft:ambient.cave", "range", 4.0),
            "@additions", List.of(map("sound", "minecraft:a", "tick_chance", 0.5), map("sound", "minecraft:b", "tick_chance", 0.25)),
            "@provider", map("type", "minecraft:uniform", "min_inclusive", "@min", "max_inclusive", 4),
            "@min", 1,
            "@flame", "minecraft:flame",
            "@message", map("translate", "@key"),
            "@key", "block.minecraft.bed.no_sleep"));

        AmbientSounds sounds = TYPES.ambientSounds().decode(map("loop", "@sound", "additions", "@additions"), context);
        assertEquals(4.0, sounds.loop().orElseThrow().range().orElseThrow());
        assertEquals(2, sounds.additions().size());

        IntProviderValue provider = TYPES.intProvider().decode("@provider", context);
        assertEquals(new RawValue(map("type", "minecraft:uniform", "min_inclusive", 1, "max_inclusive", 4)),
            provider.provider().orElseThrow());

        List<AmbientParticle> particles = TYPES.ambientParticles().decode(
            List.of(map("particle", map("type", "@flame"), "probability", 0.5)), context);
        assertEquals(new RawValue(map("type", "minecraft:flame")), particles.get(0).particle());

        BedRule rule = TYPES.bedRule().decode(map("can_sleep", "never", "can_set_spawn", "never", "error_message", "@message"),
            context);
        assertEquals(new RawValue(map("translate", "block.minecraft.bed.no_sleep")), rule.errorMessage().orElseThrow());
    }

    @Test
    void mapKeysInsideValuesAreNotPreprocessed() throws DecodeException {
        DecodeContext context = referencing(map("minecraft:zombie", "minecraft:cow"));
        MobSpawnSettings settings = TYPES.mobSpawnSettings().decode(map(
            "spawns_by_category", map(),
            "spawn_costs", map("minecraft:zombie", map("energy_budget", 1.0, "charge", 0.5))), context);
        assertEquals(List.of(RegistryKey.parse("minecraft:zombie")), List.copyOf(settings.spawnCosts().keySet()));
    }
}
