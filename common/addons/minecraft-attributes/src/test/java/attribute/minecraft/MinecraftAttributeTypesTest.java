package attribute.minecraft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.addons.attribute.minecraft.AmbientParticle;
import com.dfsek.terra.addons.attribute.minecraft.AmbientSounds;
import com.dfsek.terra.addons.attribute.minecraft.BackgroundMusic;
import com.dfsek.terra.addons.attribute.minecraft.BedRule;
import com.dfsek.terra.addons.attribute.minecraft.CardinalLight;
import com.dfsek.terra.addons.attribute.minecraft.GrassColorModifier;
import com.dfsek.terra.addons.attribute.minecraft.IntProviderValue;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributeTypes;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftCodecs;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftIds;
import com.dfsek.terra.addons.attribute.minecraft.MobSpawnSettings;
import com.dfsek.terra.addons.attribute.minecraft.MoonPhase;
import com.dfsek.terra.addons.attribute.minecraft.Music;
import com.dfsek.terra.addons.attribute.minecraft.Skybox;
import com.dfsek.terra.addons.attribute.minecraft.SoundRef;
import com.dfsek.terra.addons.attribute.minecraft.TemperatureModifier;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
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

    private static RegistryKey id(String id) {
        return RegistryKey.parse(id);
    }

    private static SoundRef sound(String id) {
        return new SoundRef(id(id), OptionalDouble.empty());
    }

    private static IntProviderValue constant(int value) {
        return new IntProviderValue(OptionalInt.of(value), Optional.empty());
    }

    private static IntProviderValue provider(Map<String, Object> raw) {
        return new IntProviderValue(OptionalInt.empty(), Optional.of(new RawValue(raw)));
    }

    private static MobSpawnSettings spawns(Map<String, Object> raw) throws DecodeException {
        return TYPES.mobSpawnSettings().decode(raw, PLAIN);
    }

    private static Map<String, Object> spawner(String type, int weight, int count) {
        return map("type", type, "weight", weight, "count", count);
    }

    @Test
    void operationSets() {
        for(AttributeType<?> t : List.of(TYPES.moonPhase(), TYPES.bedRule(), TYPES.particle(), TYPES.backgroundMusic(),
            TYPES.ambientSounds(), TYPES.identifier(), TYPES.identifierOrTag(), TYPES.intProvider())) {
            assertEquals(EnumSet.of(Operation.OVERRIDE), t.operations(), t.id());
        }
        assertEquals(EnumSet.of(Operation.OVERRIDE, Operation.APPEND), TYPES.ambientParticles().operations());
        assertEquals(EnumSet.of(Operation.OVERRIDE, Operation.OVERLAY), TYPES.mobSpawnSettings().operations());
    }

    @Test
    void enumIds() {
        assertEquals(List.of("full_moon", "waning_gibbous", "third_quarter", "waning_crescent", "new_moon", "waxing_crescent",
            "first_quarter", "waxing_gibbous"), ids(MoonPhase.values()));
        assertEquals(List.of("always", "when_dark", "never"), ids(BedRule.Rule.values()));
        assertEquals(List.of("none", "dark_forest", "swamp"), ids(GrassColorModifier.values()));
        assertEquals(List.of("none", "frozen"), ids(TemperatureModifier.values()));
        assertEquals(List.of("none", "overworld", "end"), ids(Skybox.values()));
        assertEquals(List.of("default", "nether"), ids(CardinalLight.values()));
    }

    private static List<String> ids(StringIdentifiable[] values) {
        return Arrays.stream(values).map(StringIdentifiable::getID).toList();
    }

    @Test
    void valueRecordInvariants() {
        assertThrows(IllegalArgumentException.class, () -> new IntProviderValue(OptionalInt.empty(), Optional.empty()));
        assertThrows(IllegalArgumentException.class,
            () -> new IntProviderValue(OptionalInt.of(1), Optional.of(new RawValue(Map.of("type", "minecraft:constant")))));
        LinkedHashMap<RegistryKey, MobSpawnSettings.Cost> costs = new LinkedHashMap<>();
        for(String n : new String[]{ "zombie", "alpha", "mid", "bat", "creeper", "ender" }) {
            costs.put(RegistryKey.of("minecraft", n), new MobSpawnSettings.Cost(1, 1));
        }
        MobSpawnSettings s = new MobSpawnSettings(Map.of(), costs);
        assertEquals(List.copyOf(costs.keySet()), List.copyOf(s.spawnCosts().keySet()));
        assertThrows(UnsupportedOperationException.class, () -> s.spawnCosts().clear());
    }

    static Stream<Arguments> vanillaValues() {
        Map<String, Object> uniform = map("type", "minecraft:uniform", "min_inclusive", 1, "max_inclusive", 3);
        return Stream.of(
            Arguments.of("moon phase", TYPES.moonPhase(), "new_moon", MoonPhase.NEW_MOON),
            Arguments.of("identifier", TYPES.identifier(), "minecraft:idle", id("minecraft:idle")),
            Arguments.of("bare identifier", TYPES.identifier(), "idle", id("minecraft:idle")),
            Arguments.of("bare identifier or tag", TYPES.identifierOrTag(), "stone", Either.left(id("minecraft:stone"))),
            Arguments.of("bare tag", TYPES.identifierOrTag(), "#logs", Either.right(TagKey.parse("#minecraft:logs"))),
            Arguments.of("particle", TYPES.particle(), map("type", "minecraft:dripping_dripstone_lava"),
                new RawValue(map("type", "minecraft:dripping_dripstone_lava"))),
            Arguments.of("int constant", TYPES.intProvider(), 7, constant(7)),
            Arguments.of("int provider", TYPES.intProvider(), uniform, provider(uniform)),
            Arguments.of("crimson forest particles", TYPES.ambientParticles(),
                List.of(map("particle", map("type", "minecraft:crimson_spore"), "probability", 0.025)),
                List.of(new AmbientParticle(new RawValue(map("type", "minecraft:crimson_spore")), 0.025f))),
            Arguments.of("straw bed rule", TYPES.bedRule(), map("can_set_spawn", "never", "can_sleep", "when_dark",
                    "destroy_on_leave", true, "error_message", map("translate", "block.minecraft.bed.no_sleep")),
                new BedRule(BedRule.Rule.WHEN_DARK, BedRule.Rule.NEVER, false, true,
                    Optional.of(new RawValue(map("translate", "block.minecraft.bed.no_sleep"))))),
            Arguments.of("bed rule defaults", TYPES.bedRule(), map("can_set_spawn", "always", "can_sleep", "always"),
                new BedRule(BedRule.Rule.ALWAYS, BedRule.Rule.ALWAYS, false, false, Optional.empty())),
            Arguments.of("warm ocean music", TYPES.backgroundMusic(), map(
                    "creative", map("max_delay", 24000, "min_delay", 12000, "sound", "minecraft:music.creative"),
                    "default", map("max_delay", 24000, "min_delay", 12000, "sound", "minecraft:music.game",
                        "replace_current_music", true),
                    "underwater", map("max_delay", 24000, "min_delay", 12000, "sound", "minecraft:music.under_water")),
                new BackgroundMusic(Optional.of(new Music(sound("minecraft:music.game"), 12000, 24000, true)),
                    Optional.of(new Music(sound("minecraft:music.creative"), 12000, 24000, false)),
                    Optional.of(new Music(sound("minecraft:music.under_water"), 12000, 24000, false)))),
            Arguments.of("no music", TYPES.backgroundMusic(), map(), BackgroundMusic.EMPTY),
            Arguments.of("crimson forest sounds", TYPES.ambientSounds(), map(
                    "additions", map("sound", "minecraft:ambient.crimson_forest.additions", "tick_chance", 0.0111),
                    "loop", "minecraft:ambient.crimson_forest.loop",
                    "mood", map("block_search_extent", 8, "offset", 2.0, "sound", "minecraft:ambient.crimson_forest.mood",
                        "tick_delay", 6000)),
                new AmbientSounds(Optional.of(sound("minecraft:ambient.crimson_forest.loop")),
                    Optional.of(new AmbientSounds.Mood(sound("minecraft:ambient.crimson_forest.mood"), 6000, 8, 2.0)),
                    List.of(new AmbientSounds.Additions(sound("minecraft:ambient.crimson_forest.additions"), 0.0111)))),
            Arguments.of("addition list and ranged loop", TYPES.ambientSounds(), map(
                    "loop", map("sound_id", "a:b", "range", 16),
                    "additions", List.of(map("sound", "a:b", "tick_chance", 0.1), map("sound", "ambient.cave", "tick_chance", 0.2))),
                new AmbientSounds(Optional.of(new SoundRef(id("a:b"), OptionalDouble.of(16))), Optional.empty(),
                    List.of(new AmbientSounds.Additions(sound("a:b"), 0.1), new AmbientSounds.Additions(sound("minecraft:ambient.cave"), 0.2)))),
            Arguments.of("unranged sound object", TYPES.ambientSounds(), map("loop", map("sound_id", "a:b")),
                new AmbientSounds(Optional.of(sound("a:b")), Optional.empty(), List.of())),
            Arguments.of("no sounds", TYPES.ambientSounds(), map(), AmbientSounds.EMPTY),
            Arguments.of("mob spawns", TYPES.mobSpawnSettings(), map(
                    "spawns_by_category", map("ambient", List.of(spawner("minecraft:bat", 10, 8)),
                        "monster", List.of(map("type", "minecraft:zombie", "weight", 5, "count", uniform))),
                    "spawn_costs", map("minecraft:enderman", map("energy_budget", 0.7, "charge", 0.15))),
                new MobSpawnSettings(
                    Map.of("ambient", List.of(new MobSpawnSettings.Spawner(id("minecraft:bat"), 10, constant(8))),
                        "monster", List.of(new MobSpawnSettings.Spawner(id("minecraft:zombie"), 5, provider(uniform)))),
                    Map.of(id("minecraft:enderman"), new MobSpawnSettings.Cost(0.7, 0.15)))));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("vanillaValues")
    void decodesVanillaValues(String name, AttributeType<?> type, Object raw, Object expected) throws DecodeException {
        assertEquals(expected, type.decode(raw, PLAIN));
    }

    static Stream<Arguments> invalidValues() {
        Map<String, Object> noCosts = map("spawns_by_category", map());
        return Stream.of(
            Arguments.of("moon phase", TYPES.moonPhase(), "blue_moon"),
            Arguments.of("identifier with a space", TYPES.identifier(), "Not Valid"),
            Arguments.of("identifier number", TYPES.identifier(), 3),
            Arguments.of("identifier given a tag", TYPES.identifier(), "#minecraft:logs"),
            Arguments.of("empty tag", TYPES.identifierOrTag(), "#"),
            Arguments.of("particle without type", TYPES.particle(), map("color", 1)),
            Arguments.of("particle string", TYPES.particle(), "minecraft:flame"),
            Arguments.of("int provider string", TYPES.intProvider(), "x"),
            Arguments.of("int provider fraction", TYPES.intProvider(), 1.5),
            Arguments.of("int provider overflow", TYPES.intProvider(), 99999999999L),
            Arguments.of("int provider without type", TYPES.intProvider(), map("min_inclusive", 0, "max_inclusive", 7)),
            Arguments.of("probability above 1", TYPES.ambientParticles(), List.of(map("particle", map("type", "x"), "probability", 1.5))),
            Arguments.of("missing probability", TYPES.ambientParticles(), List.of(map("particle", map("type", "x")))),
            Arguments.of("particle not an object", TYPES.ambientParticles(), List.of(map("particle", "x", "probability", 0.5))),
            Arguments.of("extra particle key", TYPES.ambientParticles(),
                List.of(map("particle", map("type", "x"), "probability", 0.5, "extra", 1))),
            Arguments.of("particles not a list", TYPES.ambientParticles(), map("particle", map("type", "x"), "probability", 0.5)),
            Arguments.of("bed rule missing can_set_spawn", TYPES.bedRule(), map("can_sleep", "always")),
            Arguments.of("extra bed rule key", TYPES.bedRule(), map("can_sleep", "always", "can_set_spawn", "always", "nope", 1)),
            Arguments.of("unknown music key", TYPES.backgroundMusic(), map("bogus", 1)),
            Arguments.of("music missing max_delay", TYPES.backgroundMusic(), map("default", map("sound", "a:b", "min_delay", 1))),
            Arguments.of("negative delay", TYPES.backgroundMusic(),
                map("default", map("sound", "a:b", "min_delay", -1, "max_delay", 1))),
            Arguments.of("misspelled sounds key", TYPES.ambientSounds(), map("looop", "a:b")),
            Arguments.of("incomplete mood", TYPES.ambientSounds(), map("mood", map("sound", "a:b"))),
            Arguments.of("sound number", TYPES.ambientSounds(), map("loop", 5)),
            Arguments.of("sound with a space", TYPES.ambientSounds(), map("loop", "Not Valid")),
            Arguments.of("sound object without ID", TYPES.ambientSounds(), map("loop", map("range", 1))),
            Arguments.of("unknown mob category", TYPES.mobSpawnSettings(),
                map("spawns_by_category", map("bogus", List.of()), "spawn_costs", map())),
            Arguments.of("missing spawn_costs", TYPES.mobSpawnSettings(), noCosts),
            Arguments.of("extra spawn settings key", TYPES.mobSpawnSettings(),
                map("spawns_by_category", map(), "spawn_costs", map(), "x", 1)),
            Arguments.of("fractional weight", TYPES.mobSpawnSettings(), map("spawns_by_category",
                map("monster", List.of(map("type", "minecraft:zombie", "weight", 1.5, "count", 1))), "spawn_costs", map())));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidValues")
    void rejectsInvalidValues(String name, AttributeType<?> type, Object raw) {
        assertThrows(DecodeException.class, () -> type.decode(raw, PLAIN));
    }

    @Test
    void nestedDecodeFailures() {
        String bedRule = assertThrows(DecodeException.class,
            () -> TYPES.bedRule().decode(map("can_sleep", "sometimes", "can_set_spawn", "always"), PLAIN)).getMessage();
        assertEquals("\"can_sleep\" of bed rule: unknown bed rule \"sometimes\", expected one of [always, when_dark, never]", bedRule);
    }

    @Test
    void ambientParticlesAppend() throws DecodeException {
        AttributeType<List<AmbientParticle>> t = TYPES.ambientParticles();
        List<AmbientParticle> one = t.decode(List.of(map("particle", map("type", "minecraft:crimson_spore"), "probability", 0.025)), PLAIN);
        List<AmbientParticle> two = t.decode(List.of(map("particle", map("type", "minecraft:white_ash"), "probability", 0.1)), PLAIN);
        assertEquals(one, t.apply(Operation.APPEND, one, List.of()));
        assertEquals(two, t.apply(Operation.APPEND, List.of(), two));
        assertEquals(List.of(one.get(0), two.get(0)),
            mod(t, Operation.APPEND, one, List.of(map("particle", map("type", "minecraft:white_ash"), "probability", 0.1))));
    }

    @Test
    void mobSpawnOverlay() throws DecodeException {
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
        MobSpawnSettings r = mod(t, Operation.OVERLAY, s, map("spawns_by_category", map("ambient", List.of(spawner("minecraft:bat", 1, 1)),
                "creature", List.of(spawner("minecraft:pig", 1, 1))),
            "spawn_costs", map("minecraft:b", map("energy_budget", 2, "charge", 2))));
        assertEquals(id("minecraft:zombie"), r.spawnsByCategory().get("monster").get(0).type());
        assertEquals(id("minecraft:pig"), r.spawnsByCategory().get("creature").get(0).type());
        assertEquals(id("minecraft:bat"), r.spawnsByCategory().get("ambient").get(0).type());
        assertEquals(Set.of(id("minecraft:a"), id("minecraft:b")), r.spawnCosts().keySet());
        MobSpawnSettings cleared = mod(t, Operation.OVERLAY, s, map("spawns_by_category", map("monster", List.of()), "spawn_costs", map()));
        assertTrue(cleared.spawnsByCategory().get("monster").isEmpty());
        assertEquals(1, cleared.spawnsByCategory().get("creature").size());
    }

    private static final List<String> ALL_CATEGORIES = List.of("monster", "creature", "ambient", "axolotls",
        "underground_water_creature", "water_creature", "water_ambient", "misc");

    private static MobSpawnSettings spawnsIn(String type, List<String> categories, String... costs) throws DecodeException {
        Map<String, Object> byCategory = new LinkedHashMap<>();
        for(String category : categories) byCategory.put(category, List.of(spawner(type, 1, 1)));
        Map<String, Object> costMap = new LinkedHashMap<>();
        for(String cost : costs) costMap.put(cost, map("energy_budget", 1, "charge", 1));
        return spawns(map("spawns_by_category", byCategory, "spawn_costs", costMap));
    }

    private static <T> void assertComposesLikeApplyingBoth(AttributeType<T> t, Operation op, List<T> subjects, List<T> arguments) {
        for(T s : subjects) {
            for(T a : arguments) {
                for(T b : arguments) {
                    Object composed = t.compose(op, a, b).orElseThrow();
                    assertEquals(t.apply(op, t.apply(op, s, a), b), t.apply(op, s, composed), s + " then " + a + " then " + b);
                }
            }
        }
    }

    @Test
    void composeEqualsApply() throws DecodeException {
        assertComposesLikeApplyingBoth(TYPES.mobSpawnSettings(), Operation.OVERLAY,
            List.of(MobSpawnSettings.EMPTY,
                spawnsIn("minecraft:zombie", List.of("monster", "creature"), "minecraft:a"),
                spawnsIn("minecraft:zombie", ALL_CATEGORIES, "minecraft:a", "minecraft:b")),
            List.of(MobSpawnSettings.EMPTY,
                spawnsIn("minecraft:husk", List.of("monster", "ambient"), "minecraft:b"),
                spawnsIn("minecraft:pig", List.of("creature"), "minecraft:c"),
                spawnsIn("minecraft:bat", ALL_CATEGORIES, "minecraft:a"),
                spawns(map("spawns_by_category", map("monster", List.of()), "spawn_costs", map()))));

        AttributeType<List<AmbientParticle>> particles = TYPES.ambientParticles();
        List<AmbientParticle> one = particles.decode(List.of(map("particle", map("type", "minecraft:crimson_spore"), "probability", 0.025)),
            PLAIN);
        List<AmbientParticle> two = particles.decode(List.of(map("particle", map("type", "minecraft:white_ash"), "probability", 0.1),
            map("particle", map("type", "minecraft:ash"), "probability", 0.5)), PLAIN);
        List<List<AmbientParticle>> lists = List.of(List.of(), one, two);
        assertComposesLikeApplyingBoth(particles, Operation.APPEND, lists, lists);
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
    void requireLowerCase() {
        RegistryKey key = id("minecraft:visual/fog_color");
        assertSame(key, MinecraftIds.requireLowerCase(key));
        assertSame(key, MinecraftIds.requireLowerCase(key, "visual/fog_color"));
        assertEquals(namespace("Mod:thing"),
            assertThrows(IllegalArgumentException.class, () -> MinecraftIds.requireLowerCase(id("Mod:thing"))).getMessage());
        assertEquals(path("mod:Thing"),
            assertThrows(IllegalArgumentException.class, () -> MinecraftIds.requireLowerCase(id("mod:Thing"))).getMessage());
        assertEquals(path("Thing"), assertThrows(IllegalArgumentException.class,
            () -> MinecraftIds.requireLowerCase(id("minecraft:Thing"), "Thing")).getMessage());
        DecodeException e = assertThrows(DecodeException.class, () -> CODECS.soundRef().decode("minecraft:Ambient.Cave", PLAIN));
        assertEquals("expected an identifier like \"minecraft:id\", got \"minecraft:Ambient.Cave\"", e.getMessage());
        assertEquals(path("minecraft:Ambient.Cave"), e.getCause().getMessage());
    }

    @Test
    void lowerCaseIds() {
        assertEquals(namespace("Minecraft:x"), lowerCaseFailure(() -> CODECS.soundRef().decode("Minecraft:x", PLAIN)));
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
        assertTrue(seen.containsAll(List.of("minecraft:music.game", 1, 2, music)), seen.toString());
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
            "@key", "block.minecraft.bed.no_sleep",
            "minecraft:zombie", "minecraft:cow"));

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

        MobSpawnSettings settings = TYPES.mobSpawnSettings().decode(map(
            "spawns_by_category", map(),
            "spawn_costs", map("minecraft:zombie", map("energy_budget", 1.0, "charge", 0.5))), context);
        assertEquals(List.of(id("minecraft:zombie")), List.copyOf(settings.spawnCosts().keySet()));
    }
}
