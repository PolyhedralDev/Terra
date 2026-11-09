package com.dfsek.terra.mod.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeScope;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.value.Color;

import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.ATTRIBUTES;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.CONTEXT;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.HANDLE;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.REGISTRIES;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.attribute;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.registry;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.vanilla;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class VanillaParentReaderTest {
    private static final RegistryOps<Object> OPS = CONTEXT.ops();
    private static final DecodeContext PLAIN = HANDLE.plainContext();
    private static final PackAttributeTable TABLE = PackAttributeTable.of(registry());
    private static final VanillaAttributeMaps MAPS = new VanillaAttributeMaps(HANDLE, CONTEXT);
    private static final VanillaParentReader READER = new VanillaParentReader(HANDLE, CONTEXT);

    private static final String AMBIENT_PARTICLES = "minecraft:visual/ambient_particles";
    private static final String FOG_COLOR = "minecraft:visual/fog_color";
    private static final String DEFAULT_CLOCK = "default_clock";

    private static final BiConsumer<Attribute<?>, String> FAIL = (attribute, reason) -> fail(attribute.getRegistryKey() + ": " + reason);

    private static Biome biome(ResourceKey<Biome> key) {
        return REGISTRIES.lookupOrThrow(Registries.BIOME).getOrThrow(key).value();
    }

    private static DimensionType dimensionType(ResourceKey<DimensionType> key) {
        return REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(key).value();
    }

    private static List<Holder.Reference<Biome>> biomes() {
        return REGISTRIES.lookupOrThrow(Registries.BIOME).listElements().toList();
    }

    private static List<Holder.Reference<DimensionType>> dimensionTypes() {
        return REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).listElements().toList();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> encodeMap(Codec<?> codec, Object value) {
        Object raw = VanillaCodecs.encode((Codec<Object>) codec, value, OPS);
        return new LinkedHashMap<>((Map<String, Object>) assertInstanceOf(Map.class, raw));
    }

    private static Map<String, Object> climate(Biome biome) {
        return encodeMap(Biome.ClimateSettings.CODEC.codec(), biome.climateSettings);
    }

    private static Map<String, Object> effects(Biome biome) {
        return encodeMap(BiomeSpecialEffects.CODEC, biome.getSpecialEffects());
    }

    private static void assertNoEntries(AttributeSlots slots, String what) {
        for(Attribute<?> attribute : ATTRIBUTES) assertNull(slots.get(attribute), what + ": " + attribute.getRegistryKey());
    }

    @Test
    void everyVanillaAttributeInTable() {
        Set<EnvironmentAttribute<?>> bound = new HashSet<>();
        for(PackAttributeTable.Bound b : TABLE.environment()) {
            assertEquals(String.valueOf(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.getKey(b.vanilla())),
                         b.attribute().getRegistryKey().toString());
            bound.add(b.vanilla());
        }
        assertEquals(Set.copyOf(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.stream().toList()), bound);
        assertEquals(51, bound.size());
        assertEquals(51, TABLE.environment().size());
        assertEquals(List.of(), TABLE.unapplied());

        assertEquals(4, TABLE.climate().size());
        assertEquals(5, TABLE.effects().size());
        assertEquals(15, TABLE.dimension().size());
        assertEquals(attribute("terra:biome/villager_type"), TABLE.villagerType());
        assertEquals(attribute("terra:world/sea_level"), TABLE.seaLevel());
        assertEquals(attribute("terra:world/spawn_height"), TABLE.spawnHeight());
        assertEquals(attribute("terra:world/mob_generation"), TABLE.mobGeneration());
    }

    @Test
    void terraScopeMatchesVanillaPositional() {
        List<String> mismatches = new ArrayList<>();
        for(PackAttributeTable.Bound b : TABLE.environment()) {
            boolean anywhere = b.attribute().scope() == AttributeScope.ANYWHERE;
            if(anywhere != b.vanilla().isPositional()) {
                mismatches.add(b.attribute().getRegistryKey() + ": Terra " + b.attribute().scope() + ", vanilla positional "
                               + b.vanilla().isPositional());
            }
        }
        assertEquals(List.of(), mismatches);
    }

    @Test
    void noRegistryMeansEmptyTable() {
        PackAttributeTable table = PackAttributeTable.of(null);
        assertEquals(List.of(), table.environment());
        assertEquals(List.of(), table.climate());
        assertEquals(List.of(), table.effects());
        assertEquals(List.of(), table.dimension());
        assertEquals(List.of(), table.unapplied());
        assertNull(table.villagerType());
        assertNull(table.seaLevel());
        assertNull(table.spawnHeight());
        assertNull(table.mobGeneration());

        VanillaParentReader reader = new VanillaParentReader(HANDLE, VanillaContext.of(REGISTRIES, key -> VillagerType.DESERT));
        assertNoEntries(reader.biome(Biomes.PLAINS, biome(Biomes.PLAINS), table, FAIL), "biome");
        assertNoEntries(reader.dimension(dimensionType(BuiltinDimensionTypes.NETHER), table, FAIL), "dimension");
        assertNoEntries(reader.generation(REGISTRIES.lookupOrThrow(Registries.NOISE_SETTINGS)
                                              .getOrThrow(NoiseGeneratorSettings.OVERWORLD).value(), table), "generation");
        assertNoEntries(AttributeSlots.EMPTY, "empty");
    }

    @Test
    void addonAttributeIsUnapplied() {
        Attribute<Float> custom = HANDLE.attributeBuilder(AttributeKey.<Float>parse("test:custom"), HANDLE.types().floatType(), 0f)
            .build();
        PackAttributeTable table = PackAttributeTable.of(registry(custom));
        assertEquals(List.of(custom), table.unapplied());
        assertEquals(51, table.environment().size());
        assertNull(READER.biome(Biomes.PLAINS, biome(Biomes.PLAINS), TABLE, FAIL).get(custom));
    }

    @Test
    void vanillaMapsRoundTrip() {
        int nonEmpty = 0;
        for(Holder.Reference<Biome> holder : biomes()) {
            if(roundTrip(holder.key().identifier().toString(), holder.value().getAttributes(), true)) nonEmpty++;
        }
        for(Holder.Reference<DimensionType> holder : dimensionTypes()) {
            if(roundTrip(holder.key().identifier().toString(), holder.value().attributes(), false)) nonEmpty++;
        }
        assertTrue(nonEmpty > 10, "few vanilla maps had entries: " + nonEmpty);
    }

    private static boolean roundTrip(String name, EnvironmentAttributeMap map, boolean positional) {
        AttributeEntry<?>[] into = new AttributeEntry<?>[HANDLE.attributeCount()];
        MAPS.toTerra(map, TABLE, into, (attribute, reason) -> fail(name + ": " + attribute.getRegistryKey() + " unreadable: " + reason));
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        for(Attribute<?> attribute : ATTRIBUTES) {
            if(into[attribute.index()] != null) entries.put(attribute, into[attribute.index()]);
        }
        assertEquals(map.keySet().size(), entries.size(), name);
        EnvironmentAttributeMap back = MAPS.toVanilla(EnvironmentAttributeMap.EMPTY, entries, positional,
                                                      (attribute, reason) -> fail(name + ": " + attribute.getRegistryKey()
                                                                                  + " skipped: " + reason));
        assertEquals(map.keySet(), back.keySet(), name);
        assertEquals(VanillaCodecs.encode(EnvironmentAttributeMap.CODEC, map, OPS),
                     VanillaCodecs.encode(EnvironmentAttributeMap.CODEC, back, OPS), name);
        return !entries.isEmpty();
    }

    @Test
    void oldParticleFormIsRejected() throws DecodeException {
        Attribute<Object> particles = attribute(AMBIENT_PARTICLES);
        Attribute<Color> fog = attribute(FOG_COLOR);
        Object oldParticle = Map.of("type", "minecraft:falling_dust", "block_state", Map.of("Name", "minecraft:sand"));
        Object newParticle = VanillaCodecs.encode(ParticleTypes.CODEC,
                                                  new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()),
                                                  OPS);

        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        entries.put(particles, HANDLE.decodeEntry(particles, List.of(Map.of("particle", oldParticle, "probability", 0.01)), PLAIN));
        entries.put(fog, HANDLE.decodeEntry(fog, "#123456", PLAIN));
        List<String> skipped = new ArrayList<>();
        EnvironmentAttributeMap rejected = MAPS.toVanilla(EnvironmentAttributeMap.EMPTY, entries, true,
                                                          (attribute, reason) -> {
                                                              assertEquals(particles, attribute);
                                                              skipped.add(reason);
                                                          });
        assertEquals(1, skipped.size());
        assertFalse(skipped.getFirst().isBlank());
        assertFalse(rejected.contains(EnvironmentAttributes.AMBIENT_PARTICLES));
        assertTrue(rejected.contains(vanilla(FOG_COLOR)), "the other entry still applies");

        AttributeEntry<Object> accepted = HANDLE.decodeEntry(particles, List.of(Map.of("particle", newParticle, "probability", 0.01)),
                                                             PLAIN);
        EnvironmentAttributeMap map = MAPS.toVanilla(EnvironmentAttributeMap.EMPTY, Map.of(particles, accepted), true, FAIL);
        List<net.minecraft.world.attribute.AmbientParticle> value = map.applyModifier(EnvironmentAttributes.AMBIENT_PARTICLES,
                                                                                      List.of());
        assertEquals(1, value.size());
        ParticleOptions particle = value.getFirst().particle();
        BlockParticleOption block = assertInstanceOf(BlockParticleOption.class, particle);
        assertEquals(Blocks.SAND.defaultBlockState(), block.getState());
    }

    @Test
    void nullOverrideKeepsBase() {
        Attribute<Color> fog = attribute(FOG_COLOR);
        EnvironmentAttributeMap base = biome(Biomes.PLAINS).getAttributes();
        EnvironmentAttributeMap result = MAPS.toVanilla(base, Map.of(fog, AttributeEntry.override(null)), true, FAIL);
        assertEquals(base, result);
    }

    @Test
    void readsBiomeFields() {
        Attribute<?> temperature = attribute("terra:biome/temperature");
        Attribute<?> precipitation = attribute("terra:biome/has_precipitation");
        Attribute<?> villager = attribute("terra:biome/villager_type");

        AttributeSlots plains = READER.biome(Biomes.PLAINS, biome(Biomes.PLAINS), TABLE, FAIL);
        assertEquals(AttributeEntry.override(0.8f), plains.get(temperature));
        assertEquals(AttributeEntry.override(true), plains.get(precipitation));
        assertNull(plains.get(villager), "the test context has no villager types");

        AttributeSlots desert = READER.biome(Biomes.DESERT, biome(Biomes.DESERT), TABLE, FAIL);
        assertEquals(AttributeEntry.override(false), desert.get(precipitation));

        assertEquals(AttributeEntry.override(Color.rgb(0x3f76e4)), plains.get(attribute("terra:biome/water_color")));
        assertNull(plains.get(attribute("terra:biome/grass_color")));

        VanillaParentReader withVillagers = new VanillaParentReader(HANDLE, VanillaContext.of(
            REGISTRIES, key -> key == Biomes.DESERT ? VillagerType.DESERT : null));
        assertEquals(AttributeEntry.override(RegistryKey.parse("minecraft:desert")),
                     withVillagers.biome(Biomes.DESERT, biome(Biomes.DESERT), TABLE, FAIL).get(villager));
        assertNull(withVillagers.biome(Biomes.PLAINS, biome(Biomes.PLAINS), TABLE, FAIL).get(villager));
    }

    @Test
    void readsBiomeEnvironment() {
        Biome plains = biome(Biomes.PLAINS);
        AttributeSlots slots = READER.biome(Biomes.PLAINS, plains, TABLE, FAIL);
        int present = 0;
        for(PackAttributeTable.Bound b : TABLE.environment()) {
            if(plains.getAttributes().contains(b.vanilla())) {
                assertNotNull(slots.get(b.attribute()), b.attribute().getRegistryKey().toString());
                present++;
            } else {
                assertNull(slots.get(b.attribute()), b.attribute().getRegistryKey().toString());
            }
        }
        assertTrue(present > 0);
    }

    @Test
    void everyTerraPathIsVanillaField() {
        Set<String> dimensionKeys = new HashSet<>();
        List<Holder.Reference<DimensionType>> types = dimensionTypes();
        assertEquals(4, types.size());
        for(Holder.Reference<DimensionType> holder : types) {
            dimensionKeys.addAll(encodeMap(DimensionType.DIRECT_CODEC, holder.value()).keySet());
        }
        for(PackAttributeTable.Field field : TABLE.dimension()) {
            assertTrue(dimensionKeys.contains(field.name()), field.name() + " is not a dimension type field: " + dimensionKeys);
        }

        Set<String> biomeKeys = new HashSet<>();
        for(Holder.Reference<Biome> holder : biomes()) {
            biomeKeys.addAll(climate(holder.value()).keySet());
            biomeKeys.addAll(effects(holder.value()).keySet());
        }
        for(PackAttributeTable.Field field : TABLE.climate()) {
            assertTrue(biomeKeys.contains(field.name()), field.name() + " is not a biome field: " + biomeKeys);
        }
        for(PackAttributeTable.Field field : TABLE.effects()) {
            assertTrue(biomeKeys.contains(field.name()), field.name() + " is not a biome field: " + biomeKeys);
        }
    }

    @Test
    void absentFieldsMatchTerraDefaults() {
        List<String> mismatches = new ArrayList<>();
        for(Holder.Reference<DimensionType> holder : dimensionTypes()) {
            absentFields(holder.key().identifier().toString(), DimensionType.DIRECT_CODEC, holder.value(), TABLE.dimension(), mismatches);
        }
        for(ResourceKey<Biome> key : List.of(Biomes.PLAINS, Biomes.SWAMP, Biomes.PALE_GARDEN)) {
            Biome biome = biome(key);
            absentFields(key.identifier().toString(), Biome.ClimateSettings.CODEC.codec(), biome.climateSettings, TABLE.climate(),
                         mismatches);
            absentFields(key.identifier().toString(), BiomeSpecialEffects.CODEC, biome.getSpecialEffects(), TABLE.effects(), mismatches);
        }
        assertEquals(List.of(), mismatches);
    }

    private static <V> void absentFields(String name, Codec<V> codec, V original, List<PackAttributeTable.Field> fields,
                                         List<String> mismatches) {
        Map<String, Object> json = encodeMap(codec, original);
        for(PackAttributeTable.Field field : fields) {
            if(field.name().equals(DEFAULT_CLOCK) || json.containsKey(field.name())) continue;
            Map<String, Object> withDefault = new LinkedHashMap<>(json);
            Object terraDefault = encodedDefault(field.attribute());
            if(terraDefault != null) withDefault.put(field.name(), terraDefault);
            try {
                Map<String, Object> parsed = encodeMap(codec, VanillaCodecs.parse(codec, withDefault, OPS));
                if(!json.equals(parsed)) {
                    mismatches.add(name + " " + field.name() + ": Terra default " + terraDefault + " reads as " + parsed.get(field.name()));
                }
            } catch(DecodeException e) {
                mismatches.add(name + " " + field.name() + ": vanilla rejects Terra default " + terraDefault + ": " + e.getMessage());
            }
        }
    }

    private static <T> Object encodedDefault(Attribute<T> attribute) {
        T value = attribute.defaultValue();
        return value == null ? null : attribute.type().encode(value);
    }

    @Test
    void readsDimensionFields() {
        AttributeSlots overworld = READER.dimension(dimensionType(BuiltinDimensionTypes.OVERWORLD), TABLE, FAIL);
        assertEquals(AttributeEntry.override(-64), overworld.get(attribute("terra:dimension/min_y")));
        assertEquals(AttributeEntry.override(384), overworld.get(attribute("terra:dimension/height")));
        assertEquals(AttributeEntry.override(true), overworld.get(attribute("terra:dimension/has_skylight")));
        assertEquals(AttributeEntry.override(RegistryKey.parse("minecraft:overworld")),
                     overworld.get(attribute("terra:dimension/default_clock")));

        AttributeSlots nether = READER.dimension(dimensionType(BuiltinDimensionTypes.NETHER), TABLE, FAIL);
        assertEquals(AttributeEntry.override(null), nether.get(attribute("terra:dimension/default_clock")));
        assertEquals(AttributeEntry.override(true), nether.get(attribute("terra:dimension/has_ceiling")));
        assertEquals(AttributeEntry.override(4f), nether.get(attribute("minecraft:gameplay/sky_light_level")));
    }

    @Test
    void readsGeneration() {
        NoiseGeneratorSettings overworld = REGISTRIES.lookupOrThrow(Registries.NOISE_SETTINGS)
            .getOrThrow(NoiseGeneratorSettings.OVERWORLD).value();
        AttributeSlots slots = READER.generation(overworld, TABLE);
        assertEquals(AttributeEntry.override(63), slots.get(attribute("terra:world/sea_level")));
        assertEquals(AttributeEntry.override(true), slots.get(attribute("terra:world/mob_generation")));
        assertNull(slots.get(attribute("terra:world/spawn_height")));

        NoiseGeneratorSettings nether = REGISTRIES.lookupOrThrow(Registries.NOISE_SETTINGS)
            .getOrThrow(NoiseGeneratorSettings.NETHER).value();
        assertEquals(AttributeEntry.override(32), READER.generation(nether, TABLE).get(attribute("terra:world/sea_level")));
    }
}
