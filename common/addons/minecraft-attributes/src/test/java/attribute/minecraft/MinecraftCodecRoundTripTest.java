package attribute.minecraft;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import com.dfsek.terra.addons.attribute.minecraft.AmbientParticle;
import com.dfsek.terra.addons.attribute.minecraft.AmbientSounds;
import com.dfsek.terra.addons.attribute.minecraft.BackgroundMusic;
import com.dfsek.terra.addons.attribute.minecraft.BedRule;
import com.dfsek.terra.addons.attribute.minecraft.CardinalLight;
import com.dfsek.terra.addons.attribute.minecraft.GrassColorModifier;
import com.dfsek.terra.addons.attribute.minecraft.IntProviderValue;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributeTypes;
import com.dfsek.terra.addons.attribute.minecraft.MobSpawnSettings;
import com.dfsek.terra.addons.attribute.minecraft.MoonPhase;
import com.dfsek.terra.addons.attribute.minecraft.Music;
import com.dfsek.terra.addons.attribute.minecraft.Skybox;
import com.dfsek.terra.addons.attribute.minecraft.SoundRef;
import com.dfsek.terra.addons.attribute.minecraft.TemperatureModifier;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.RawValue;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class MinecraftCodecRoundTripTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final AttributeTypes CORE = HANDLE.types();
    private static final MinecraftAttributeTypes TYPES = new MinecraftAttributeTypes(HANDLE);
    private static final DecodeContext PLAIN = HANDLE.plainContext();

    private static final RawValue FLAME = new RawValue(map("type", "minecraft:flame"));
    private static final RawValue UNIFORM = new RawValue(map("type", "minecraft:uniform", "min_inclusive", 1, "max_inclusive", 3));
    private static final SoundRef SOUND = new SoundRef(RegistryKey.parse("minecraft:music.game"), OptionalDouble.empty());
    private static final SoundRef RANGED_SOUND = new SoundRef(RegistryKey.parse("minecraft:ambient.cave"), OptionalDouble.of(16));

    private static Map<String, Object> map(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for(int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }

    private record Case<T>(AttributeType<T> type, List<T> values, Map<Operation, List<?>> arguments) { }

    private static <T> Case<T> of(AttributeType<T> type, List<T> values, Object... opArgs) {
        Map<Operation, List<?>> arguments = new LinkedHashMap<>();
        for(int i = 0; i < opArgs.length; i += 2) arguments.put((Operation) opArgs[i], (List<?>) opArgs[i + 1]);
        return new Case<>(type, values, arguments);
    }

    private static List<Case<?>> cases() {
        MobSpawnSettings spawns = new MobSpawnSettings(
            Map.of("monster", List.of(
                    new MobSpawnSettings.Spawner(RegistryKey.parse("minecraft:zombie"), 95,
                        new IntProviderValue(OptionalInt.of(4), Optional.empty())),
                    new MobSpawnSettings.Spawner(RegistryKey.parse("minecraft:husk"), 5,
                        new IntProviderValue(OptionalInt.empty(), Optional.of(UNIFORM)))),
                "ambient", List.of()),
            Map.of(RegistryKey.parse("minecraft:enderman"), new MobSpawnSettings.Cost(0.7, 0.15)));

        return List.of(
            of(TYPES.moonPhase(), List.of(MoonPhase.values())),
            of(TYPES.bedRule(), List.of(
                new BedRule(BedRule.Rule.WHEN_DARK, BedRule.Rule.NEVER, true, false,
                    Optional.of(new RawValue(map("translate", "block.minecraft.bed.no_sleep")))),
                new BedRule(BedRule.Rule.ALWAYS, BedRule.Rule.ALWAYS, false, true, Optional.empty()))),
            of(TYPES.particle(), List.of(FLAME, new RawValue(map("type", "minecraft:dust", "color", List.of(1.0, 0.5, 0.0), "scale", 2)))),
            of(TYPES.ambientParticles(), List.of(List.of(), List.of(new AmbientParticle(FLAME, 0.025f))),
                Operation.APPEND, List.of(List.of(new AmbientParticle(FLAME, 0.5f), new AmbientParticle(FLAME, 1f)))),
            of(TYPES.backgroundMusic(), List.of(BackgroundMusic.EMPTY,
                new BackgroundMusic(Optional.of(new Music(SOUND, 12000, 24000, true)), Optional.empty(),
                    Optional.of(new Music(RANGED_SOUND, 0, 1, false))))),
            of(TYPES.ambientSounds(), List.of(AmbientSounds.EMPTY,
                new AmbientSounds(Optional.of(RANGED_SOUND), Optional.of(new AmbientSounds.Mood(SOUND, 6000, 8, 2.0)),
                    List.of(new AmbientSounds.Additions(SOUND, 0.0111), new AmbientSounds.Additions(RANGED_SOUND, 0.5))))),
            of(TYPES.mobSpawnSettings(), List.of(MobSpawnSettings.EMPTY, spawns),
                Operation.OVERLAY, List.of(MobSpawnSettings.EMPTY, spawns)),
            of(TYPES.identifier(), List.of(RegistryKey.parse("minecraft:stone"), RegistryKey.parse("my.pack:a/b"))),
            of(TYPES.identifierOrTag(), List.<Either<RegistryKey, TagKey>>of(
                Either.left(RegistryKey.parse("minecraft:stone")), Either.right(TagKey.parse("#my.pack:logs")))),
            of(TYPES.intProvider(), List.of(new IntProviderValue(OptionalInt.of(-4), Optional.empty()),
                new IntProviderValue(OptionalInt.empty(), Optional.of(UNIFORM)))),
            of(CORE.enumType("skybox", Skybox.class), List.of(Skybox.values())),
            of(CORE.enumType("cardinal_light", CardinalLight.class), List.of(CardinalLight.values())),
            of(CORE.enumType("grass_color_modifier", GrassColorModifier.class), List.of(GrassColorModifier.values())),
            of(CORE.enumType("temperature_modifier", TemperatureModifier.class), List.of(TemperatureModifier.values())));
    }

    private static void assertPlain(Object encoded, String where) {
        if(encoded == null || encoded instanceof Number || encoded instanceof Boolean || encoded instanceof String) return;
        if(encoded instanceof List<?> list) {
            for(Object element : list) assertPlain(element, where);
            return;
        }
        if(encoded instanceof Map<?, ?> m) {
            m.forEach((k, v) -> {
                assertTrue(k instanceof String, where + ": map key " + k + " is not a string");
                assertPlain(v, where);
            });
            return;
        }
        fail(where + ": encoded a " + encoded.getClass().getName() + ": " + encoded);
    }

    private static <T> void assertValuesRoundTrip(Case<T> c) throws DecodeException {
        for(T value : c.values()) {
            Object encoded = c.type().encode(value);
            assertPlain(encoded, c.type().id());
            assertEquals(value, c.type().decode(encoded, PLAIN), c.type().id() + " via " + encoded);
        }
    }

    @Test
    void everyMinecraftType() throws DecodeException {
        for(Case<?> c : cases()) assertValuesRoundTrip(c);
    }

    @Test
    void everyArgument() throws DecodeException {
        for(Case<?> c : cases()) {
            AttributeType<?> type = c.type();
            List<Operation> unsampled = new ArrayList<>(type.operations());
            unsampled.remove(Operation.OVERRIDE);
            unsampled.removeAll(c.arguments().keySet());
            assertTrue(unsampled.isEmpty(), type.id() + " has no sample argument for " + unsampled);

            Map<Operation, List<?>> arguments = new LinkedHashMap<>(c.arguments());
            arguments.put(Operation.OVERRIDE, c.values());
            for(Map.Entry<Operation, List<?>> e : arguments.entrySet()) {
                for(Object argument : e.getValue()) {
                    Object encoded = type.encodeArgument(e.getKey(), argument);
                    assertPlain(encoded, type.id() + " " + e.getKey().getID());
                    assertEquals(argument, type.decodeArgument(e.getKey(), encoded, PLAIN),
                        type.id() + " " + e.getKey().getID() + " via " + encoded);
                }
            }
        }
    }

    @Test
    void canonicalForms() {
        assertEquals("minecraft:stone", TYPES.identifier().encode(RegistryKey.parse("minecraft:stone")));
        assertEquals("#minecraft:logs", TYPES.identifierOrTag().encode(Either.right(TagKey.parse("#minecraft:logs"))));
        assertEquals("minecraft:stone", TYPES.identifierOrTag().encode(Either.left(RegistryKey.parse("minecraft:stone"))));
        assertEquals("new_moon", TYPES.moonPhase().encode(MoonPhase.NEW_MOON));
        assertEquals("end", CORE.enumType("skybox", Skybox.class).encode(Skybox.END));
        assertEquals(map("type", "minecraft:flame"), TYPES.particle().encode(FLAME));
        assertEquals(7, TYPES.intProvider().encode(new IntProviderValue(OptionalInt.of(7), Optional.empty())));
        Map<?, ?> plainLoop = (Map<?, ?>) TYPES.ambientSounds().encode(new AmbientSounds(Optional.of(SOUND), Optional.empty(), List.of()));
        assertEquals("minecraft:music.game", plainLoop.get("loop"));
        Map<?, ?> rangedLoop = (Map<?, ?>) TYPES.ambientSounds().encode(
            new AmbientSounds(Optional.of(RANGED_SOUND), Optional.empty(), List.of()));
        assertEquals(map("sound_id", "minecraft:ambient.cave", "range", 16.0), rangedLoop.get("loop"));
        Map<?, ?> bedRule = (Map<?, ?>) TYPES.bedRule().encode(
            new BedRule(BedRule.Rule.NEVER, BedRule.Rule.NEVER, false, false, Optional.empty()));
        assertFalse(bedRule.containsKey("error_message"));
    }
}
