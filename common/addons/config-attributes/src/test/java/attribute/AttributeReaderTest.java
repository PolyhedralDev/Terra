package attribute;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

import com.dfsek.terra.addons.attribute.AttributeReader;
import com.dfsek.terra.addons.attribute.AttributeReader.Problem;
import com.dfsek.terra.addons.attribute.AttributeReader.Result;
import com.dfsek.terra.addons.attribute.AttributeReader.Scope;
import com.dfsek.terra.addons.attribute.RegistryAttributeLookup;
import com.dfsek.terra.addons.attribute.minecraft.IntProviderValue;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributeTable;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributes;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.*;


class AttributeReaderTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final Registry<Attribute<?>> REGISTRY = registry();
    private static final AttributeReader READER = new AttributeReader(REGISTRY, HANDLE);
    private static final String LIGHT = "terra:dimension/monster_spawn_light_level";
    private static final String NAMESPACE_REQUIRED = "attribute keys must include a namespace, e.g. \"minecraft:visual/fog_color\"";

    private static Registry<Attribute<?>> registry() {
        OpenRegistryImpl<Attribute<?>> registry = new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE);
        for(Attribute<?> attribute : MinecraftAttributeTable.create(HANDLE)) registry.register(attribute.getRegistryKey(), attribute);
        return registry;
    }

    private static Map<String, Object> map(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for(int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }

    private static void assertExactProblem(Result result, String key, String reason) {
        assertEquals(List.of(new Problem(key, reason)), result.problems());
    }

    static Stream<Arguments> accepted() {
        return Stream.of(
            Arguments.of(Scope.BIOME, "minecraft:visual/fog_color", "#c0d8ff", Operation.OVERRIDE),
            Arguments.of(Scope.BIOME, "minecraft:visual/water_fog_end_distance", map("modifier", "multiply", "argument", 0.85),
                Operation.MULTIPLY),
            Arguments.of(Scope.BIOME, "minecraft:visual/star_brightness", map("modifier", "multiply", "argument", 5), Operation.MULTIPLY),
            Arguments.of(Scope.BIOME, "minecraft:visual/ambient_particles", map("modifier", "append",
                "argument", List.of(map("particle", map("type", "minecraft:crimson_spore"), "probability", 0.025))), Operation.APPEND),
            Arguments.of(Scope.BIOME, "minecraft:gameplay/natural_mob_spawns", map("modifier", "overlay", "argument", map(
                "spawn_costs", map("minecraft:enderman", map("energy_budget", 0.7, "charge", 0.15)),
                "spawns_by_category", map("creature", List.of(
                    map("type", "minecraft:sheep", "count", 4, "weight", 12),
                    map("type", "minecraft:horse", "weight", 5,
                        "count", map("type", "minecraft:uniform", "max_inclusive", 6, "min_inclusive", 2)))))), Operation.OVERLAY),
            Arguments.of(Scope.PACK, "minecraft:gameplay/fast_lava", true, Operation.OVERRIDE),
            Arguments.of(Scope.PACK, LIGHT, map("type", "minecraft:uniform", "min_inclusive", 0, "max_inclusive", 7), Operation.OVERRIDE));
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("accepted")
    void accepts(Scope scope, String key, Object value, Operation operation) {
        Result r = READER.read(Map.of(key, value), scope);
        assertTrue(r.problems().isEmpty(), r.problems().toString());
        assertEquals(operation, r.map().get(AttributeKey.parse(key)).orElseThrow().operation());
    }

    @Test
    void intProviderModifierForm() {
        Result r = READER.read(Map.of(LIGHT, Map.of("modifier", "override", "argument", 7)), Scope.PACK);
        assertTrue(r.problems().isEmpty(), r.problems().toString());
        AttributeEntry<?> e = r.map().get(MinecraftAttributes.DIMENSION_MONSTER_SPAWN_LIGHT_LEVEL).orElseThrow();
        assertEquals(Operation.OVERRIDE, e.operation());
        assertEquals(new IntProviderValue(OptionalInt.of(7), Optional.empty()), e.argument());
    }

    static Stream<Arguments> problems() {
        return Stream.of(
            Arguments.of(Scope.BIOME, "minecraft:nope", 1, "unknown attribute"),
            Arguments.of(Scope.BIOME, "minecraft:Visual/Sky_Color", "#123456", "unknown attribute"),
            Arguments.of(Scope.BIOME, "visual/sky_color", "#123456", NAMESPACE_REQUIRED),
            Arguments.of(Scope.BIOME, "minecraft:Bad Key!", 1, "invalid attribute key..."),
            Arguments.of(Scope.BIOME, "minecraft:visual/sky_color", true, "expected..."),
            Arguments.of(Scope.BIOME, "minecraft:audio/ambient_sounds", map("loop", "minecraft:Music.Game"),
                "\"loop\" of ambient sounds: expected an identifier like \"minecraft:id\", got \"minecraft:Music.Game\""),
            Arguments.of(Scope.BIOME, "minecraft:visual/star_brightness", 2, "value 2.0 is outside [0, 1]"),
            Arguments.of(Scope.BIOME, "minecraft:visual/star_brightness", map("modifier", "override", "argument", 2),
                "value 2.0 is outside [0, 1]"),
            Arguments.of(Scope.BIOME, "minecraft:visual/star_brightness", map("modifier", "and", "argument", true),
                "modifier and is not valid for minecraft:visual/star_brightness"),
            Arguments.of(Scope.BIOME, "minecraft:visual/star_brightness", map("modifier", "frobnicate", "argument", 1),
                "unknown modifier \"frobnicate\""),
            Arguments.of(Scope.BIOME, "minecraft:visual/star_brightness", map("modifier", "add", "argument", null),
                "expected a number, got nothing"),
            Arguments.of(Scope.PACK, LIGHT, map("modifier", "frobnicate", "argument", 7), "unknown modifier \"frobnicate\""),
            Arguments.of(Scope.PACK, LIGHT, map("min_inclusive", 0, "max_inclusive", 7), "int provider object is missing \"type\""),
            Arguments.of(Scope.PACK, "terra:dimension/height", 100, "value 100 is not a multiple of 16"),
            Arguments.of(Scope.BIOME, "minecraft:gameplay/fast_lava", true, "not allowed in a biome"),
            Arguments.of(Scope.BIOME, "minecraft:visual/ambient_particles", Arrays.asList((Object) null),
                "element 0: expected an object for ambient particle, got nothing"),
            Arguments.of(Scope.BIOME, "minecraft:visual/ambient_particles", List.of(map("particle", null, "probability", 0.1)),
                "element 0: \"particle\" of ambient particle: expected a particle object with a \"type\" key, got nothing"));
    }

    @ParameterizedTest(name = "{1}: {3}")
    @MethodSource("problems")
    void badEntryIsolated(Scope scope, String key, Object value, String reason) {
        Result r = READER.read(map(key, value, "minecraft:visual/fog_color", "#c0d8ff"), scope);
        assertEquals(1, r.problems().size(), r.problems().toString());
        assertEquals(key, r.problems().get(0).key());
        if(reason.endsWith("...")) {
            String prefix = reason.substring(0, reason.length() - 3);
            assertTrue(r.problems().get(0).reason().startsWith(prefix), r.problems().get(0).reason());
        } else {
            assertEquals(reason, r.problems().get(0).reason());
        }
        assertTrue(r.map().get(MinecraftAttributes.VISUAL_FOG_COLOR).isPresent());
        assertEquals(1, r.map().keys().size());
    }

    @Test
    void nullUnsetsKnownKeys() {
        for(Scope scope : Scope.values()) {
            Result r = READER.read(map("minecraft:visual/fog_color", null, "minecraft:visual/sky_color", "#405060"), scope);
            assertTrue(r.problems().isEmpty(), r.problems().toString());
            assertTrue(r.map().get(MinecraftAttributes.VISUAL_FOG_COLOR).isEmpty());
            assertEquals(1, r.map().keys().size());
        }
        Map<String, Object> raw = map("minecraft:nope", null, "minecraft:Bad Key!", null, "visual/sky_color", null,
            "terra:world/sea_level", null, "minecraft:visual/fog_color", null);
        Result r = READER.read(raw, Scope.BIOME);
        assertEquals(List.of(
            new Problem("minecraft:nope", "unknown attribute"),
            new Problem("minecraft:Bad Key!", "invalid attribute key: ID must only contain alphanumeric characters, hyphens, underscores, periods, and slashes. \"Bad Key!\" is not a valid ID."),
            new Problem("visual/sky_color", NAMESPACE_REQUIRED),
            new Problem("terra:world/sea_level", "not allowed in a biome")),
            r.problems());
        assertTrue(r.map().isEmpty());
        assertTrue(READER.read(map("terra:world/sea_level", null), Scope.PACK).problems().isEmpty());
    }

    @Test
    void unknownKeyHint() {
        AttributeReader empty = new AttributeReader(new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE), HANDLE);
        for(String key : List.of("minecraft:visual/fog_color", "terra:biome/temperature")) {
            String namespace = key.substring(0, key.indexOf(':'));
            assertExactProblem(empty.read(Map.of(key, "#ffffff"), Scope.BIOME), key,
                "unknown attribute: no \"" + namespace + "\" attributes are registered; add the \"minecraft-attributes\" addon "
                + "to the pack's addons");
        }
        Result none = new AttributeReader(null, HANDLE).read(Map.of("minecraft:visual/fog_color", "#ffffff"), Scope.PACK);
        assertExactProblem(none, "minecraft:visual/fog_color", "unknown attribute: no \"minecraft\" attributes are registered; add the "
                                                               + "\"minecraft-attributes\" addon to the pack's addons");
        assertExactProblem(empty.read(Map.of("mypack:thing", 1), Scope.BIOME), "mypack:thing", "unknown attribute");
        assertExactProblem(READER.read(Map.of("minecraft:visual/fog_colour", "#ffffff"), Scope.BIOME), "minecraft:visual/fog_colour",
            "unknown attribute");
    }

    @Test
    void scheduleTrackKeys() {
        Map<String, Object> track = Map.of("keyframes", List.of(Map.of("ticks", 0, "value", 10.0)));
        Map<String, Object> raw = map("clock", "minecraft:overworld", "tracks", map(
            "visual/sun_angle", track,
            "minecraft:visual/sun angle", track,
            "minecraft:visual/moon_angle", track));
        ScheduleDecodeResult r = HANDLE.decodeSchedule(RegistryKey.parse("terra:test"), raw, new RegistryAttributeLookup(REGISTRY),
            HANDLE.plainContext());
        assertEquals(List.of(
            new ScheduleDecodeResult.Problem("visual/sun_angle", NAMESPACE_REQUIRED),
            new ScheduleDecodeResult.Problem("minecraft:visual/sun angle", "invalid attribute key: ID must only contain alphanumeric characters, hyphens, underscores, periods, and slashes. \"visual/sun angle\" is not a valid ID.")),
            r.droppedTracks());
        assertEquals(1, r.schedule().orElseThrow().tracks().size());
    }
}
