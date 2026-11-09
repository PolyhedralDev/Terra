package com.dfsek.terra.minecraft.attribute;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.junit.jupiter.api.Test;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributes;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.world.attribute.platform.AttributeReport;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;

import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.CONTEXT;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.HANDLE;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.REGISTRIES;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.attribute;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.registry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class MinecraftAttributeParentsTest {
    private static final com.dfsek.terra.api.registry.Registry<Attribute<?>> REGISTRY = registry();
    private static final ConfigPack PACK = pack("test", REGISTRY);

    private static final Attribute<Boolean> RESPAWN_ANCHOR = attr(MinecraftAttributes.GAMEPLAY_RESPAWN_ANCHOR_WORKS);
    private static final Attribute<Float> SKY_LIGHT = attr(MinecraftAttributes.GAMEPLAY_SKY_LIGHT_LEVEL);
    private static final Attribute<Boolean> HAS_CEILING = attr(MinecraftAttributes.DIMENSION_HAS_CEILING);
    private static final Attribute<Integer> SEA_LEVEL = attr(MinecraftAttributes.WORLD_SEA_LEVEL);
    private static final Attribute<Boolean> PRECIPITATION = attr(MinecraftAttributes.BIOME_HAS_PRECIPITATION);

    private static final BiConsumer<Attribute<?>, String> FAIL = (attribute, reason) -> fail(attribute.getRegistryKey() + ": " + reason);

    private final List<String> warnings = new ArrayList<>();
    private final List<String> debugs = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, debugs::add);
    private final MinecraftAttributeParents parents = new MinecraftAttributeParents(HANDLE);

    private static <T> Attribute<T> attr(AttributeKey<T> key) {
        return attribute(key.getRegistryKey().toString());
    }

    static ConfigPack pack(String id, com.dfsek.terra.api.registry.Registry<Attribute<?>> attributes) {
        return (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getID" -> id;
                case "getRegistry" -> attributes;
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                case "toString" -> "ConfigPack[" + id + "]";
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private FakePackAttributes fake(Map<String, RegistryKey> parentIds) {
        return new FakePackAttributes(HANDLE, parents, AttributeMap.empty(), parentIds, Map.of());
    }

    private static <T> T value(ResourceKey<? extends Registry<T>> registry, ResourceKey<T> key) {
        return REGISTRIES.lookupOrThrow(registry).getOrThrow(key).value();
    }

    private static PackAttributeTable table() {
        return PackAttributeTable.of(REGISTRY);
    }

    @Test
    void overworldByDefault() {
        parents.beginLoad(CONTEXT);
        FakePackAttributes fake = fake(Map.of());
        PackParents pack = parents.prepare(PACK, Optional.of(fake), report);

        DimensionType overworld = value(Registries.DIMENSION_TYPE, BuiltinDimensionTypes.OVERWORLD);
        AttributeSlots expected = new VanillaParentReader(HANDLE, CONTEXT).dimension(overworld, table(), FAIL);
        assertEquals(expected.get(RESPAWN_ANCHOR), parents.packOrNull(fake, RESPAWN_ANCHOR));
        assertFalse(fake.resolvePack(RESPAWN_ANCHOR));
        assertEquals(AttributeEntry.override(false), parents.packOrNull(fake, HAS_CEILING));
        assertEquals(AttributeEntry.override(63), parents.packOrNull(fake, SEA_LEVEL));

        assertEquals(BuiltinDimensionTypes.OVERWORLD, pack.dimensionKey());
        assertSame(overworld, pack.dimension());
        assertEquals(NoiseGeneratorSettings.OVERWORLD, pack.generationKey());
        assertSame(value(Registries.NOISE_SETTINGS, NoiseGeneratorSettings.OVERWORLD), pack.generation());
        assertSame(fake, pack.attributes());
        assertSame(report, pack.report());
        assertEquals(List.of(), warnings);

        PackParents bare = parents.prepare(pack("bare", REGISTRY), Optional.empty(), report);
        assertNull(bare.attributes());
        assertEquals(BuiltinDimensionTypes.OVERWORLD, bare.dimensionKey());
        assertEquals(NoiseGeneratorSettings.OVERWORLD, bare.generationKey());
        assertEquals(AttributeEntry.override(63), bare.packEntries().get(SEA_LEVEL));
    }

    @Test
    void namedParentsAreUsed() {
        parents.beginLoad(CONTEXT);
        FakePackAttributes dimension = fake(Map.of("vanilla", RegistryKey.parse("minecraft:the_nether")));
        PackParents nether = parents.prepare(PACK, Optional.of(dimension), report);
        assertEquals(BuiltinDimensionTypes.NETHER, nether.dimensionKey());
        assertSame(value(Registries.DIMENSION_TYPE, BuiltinDimensionTypes.NETHER), nether.dimension());
        assertEquals(AttributeEntry.override(true), parents.packOrNull(dimension, HAS_CEILING));
        assertEquals(AttributeEntry.override(4f), parents.packOrNull(dimension, SKY_LIGHT));
        assertEquals(NoiseGeneratorSettings.OVERWORLD, nether.generationKey());
        assertEquals(AttributeEntry.override(63), parents.packOrNull(dimension, SEA_LEVEL));

        FakePackAttributes generation = fake(Map.of("vanilla-generation", RegistryKey.parse("minecraft:nether")));
        PackParents netherGeneration = parents.prepare(PACK, Optional.of(generation), report);
        assertEquals(NoiseGeneratorSettings.NETHER, netherGeneration.generationKey());
        assertEquals(AttributeEntry.override(32), parents.packOrNull(generation, SEA_LEVEL));
        assertEquals(BuiltinDimensionTypes.OVERWORLD, netherGeneration.dimensionKey());
        assertEquals(List.of(), warnings);
    }

    @Test
    void givenDefaultDimension() {
        parents.beginLoad(CONTEXT);
        FakePackAttributes fake = fake(Map.of());
        PackParents nether = parents.prepare(PACK, Optional.of(fake), report, BuiltinDimensionTypes.NETHER);
        assertEquals(BuiltinDimensionTypes.NETHER, nether.dimensionKey());
        assertTrue(nether.dimensionDefaulted());
        assertEquals(AttributeEntry.override(true), nether.packEntries().get(HAS_CEILING));
        assertSame(nether, parents.prepare(PACK, Optional.of(fake), report, BuiltinDimensionTypes.NETHER));

        PackParents overworld = parents.prepare(PACK, Optional.of(fake), report);
        assertNotSame(nether, overworld);
        assertEquals(BuiltinDimensionTypes.OVERWORLD, overworld.dimensionKey());
        assertTrue(overworld.dimensionDefaulted());

        ResourceKey<DimensionType> missing = ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.parse("terra:nope"));
        assertSame(overworld, parents.prepare(PACK, Optional.of(fake), report, missing));

        FakePackAttributes named = fake(Map.of("vanilla", RegistryKey.parse("minecraft:the_nether")));
        PackParents explicit = parents.prepare(PACK, Optional.of(named), report, BuiltinDimensionTypes.OVERWORLD);
        assertEquals(BuiltinDimensionTypes.NETHER, explicit.dimensionKey());
        assertFalse(explicit.dimensionDefaulted());
        assertSame(explicit, parents.prepare(PACK, Optional.of(named), report, BuiltinDimensionTypes.END));
        assertEquals(List.of(), warnings);
    }

    @Test
    void badParentWarnsAndFallsBack() {
        parents.beginLoad(CONTEXT);
        PackParents upper = parents.prepare(PACK, Optional.of(fake(Map.of("vanilla", RegistryKey.parse("minecraft:The_Nether")))),
                                            report);
        FakePackAttributes unknownIds = fake(Map.of("vanilla", RegistryKey.parse("minecraft:nope"),
                                                    "vanilla-generation", RegistryKey.parse("minecraft:nope")));
        PackParents unknown = parents.prepare(PACK, Optional.of(unknownIds), report);

        assertEquals(List.of(
            "Pack \"test\", config \"pack.yml\": \"vanilla\" ignored: invalid ID \"minecraft:The_Nether\": Minecraft IDs use only "
            + "lower-case a-z, 0-9, _, . and -, and / in the path",
            "Pack \"test\", config \"pack.yml\": \"vanilla\" ignored: no such dimension type \"minecraft:nope\"",
            "Pack \"test\", config \"pack.yml\": \"vanilla-generation\" ignored: no such noise settings \"minecraft:nope\""),
            warnings);
        for(PackParents pack : List.of(upper, unknown)) {
            assertEquals(BuiltinDimensionTypes.OVERWORLD, pack.dimensionKey());
            assertEquals(NoiseGeneratorSettings.OVERWORLD, pack.generationKey());
            assertEquals(AttributeEntry.override(false), pack.packEntries().get(HAS_CEILING));
        }

        assertSame(unknown, parents.prepare(PACK, Optional.of(unknownIds), report));
        assertEquals(3, warnings.size(), warnings.toString());
        PackParents bare = parents.prepare(PACK, Optional.empty(), report);
        assertNotSame(unknown, bare);
        assertSame(bare, parents.prepare(PACK, Optional.empty(), report));
    }

    @Test
    void unknownParentCreatesNoHolder() {
        MappedRegistry<DimensionType> dimensionTypes = VanillaContextTest.copy(Registries.DIMENSION_TYPE);
        MappedRegistry<NoiseGeneratorSettings> noiseSettings = VanillaContextTest.copy(Registries.NOISE_SETTINGS);
        MappedRegistry<Biome> biomes = new MappedRegistry<>(Registries.BIOME, Lifecycle.stable());
        VanillaContext load = VanillaContext.ofLoad(List.of(dimensionTypes, noiseSettings, biomes), MinecraftTestSupport.loadContext(),
                                                    key -> null);
        parents.beginLoad(new VanillaContext(CONTEXT.ops(), load.dimensionTypes(), load.noiseSettings(), load.biomes(), key -> null));
        dimensionTypes.freeze();
        noiseSettings.freeze();

        PackParents pack = parents.prepare(PACK, Optional.of(fake(Map.of("vanilla", RegistryKey.parse("minecraft:nope"),
                                                                         "vanilla-generation", RegistryKey.parse("minecraft:nope")))),
                                           report);

        assertEquals(BuiltinDimensionTypes.OVERWORLD, pack.dimensionKey());
        assertEquals(NoiseGeneratorSettings.OVERWORLD, pack.generationKey());
        assertEquals(2, warnings.size(), warnings.toString());
        assertFalse(dimensionTypes.containsKey(ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.parse("minecraft:nope"))));
        assertFalse(noiseSettings.containsKey(ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.parse("minecraft:nope"))));
    }

    @Test
    void biomeOrNullReadsEntryHolder() {
        parents.beginLoad(CONTEXT);
        PackParents pack = parents.prepare(PACK, Optional.empty(), report);
        TestBiome biome = new TestBiome("BIOME", 0, Biomes.DESERT.identifier());
        assertSame(AttributeSlots.EMPTY, biome.platform().parentEntries());
        assertNull(parents.biomeOrNull(biome, PRECIPITATION));

        AttributeSlots desert = parents.biomeEntries(pack, Biomes.DESERT, value(Registries.BIOME, Biomes.DESERT));
        assertSame(desert, parents.biomeEntries(pack, Biomes.DESERT, value(Registries.BIOME, Biomes.DESERT)), "cached per parent key");
        biome.platform().setParentEntries(desert);

        assertEquals(AttributeEntry.override(false), parents.biomeOrNull(biome, PRECIPITATION));
        PlatformBiome other = () -> "other";
        assertNull(parents.biomeOrNull(new TestBiome("OTHER", 1, other), PRECIPITATION));
        assertEquals(List.of(), warnings);
    }

    @Test
    void unreadableBiomeParentIsReportedOnce() {
        Attribute<RegistryKey> downfall = HANDLE.attributeBuilder(AttributeKey.<RegistryKey>parse("terra:biome/downfall"),
                                                                  HANDLE.types().identifier("minecraft"),
                                                                  RegistryKey.parse("minecraft:none")).build();
        parents.beginLoad(CONTEXT);
        PackParents pack = parents.prepare(pack("test", registry(downfall)), Optional.empty(), report);
        Biome plains = value(Registries.BIOME, Biomes.PLAINS);

        AttributeSlots first = parents.biomeEntries(pack, Biomes.PLAINS, plains);
        assertSame(first, parents.biomeEntries(pack, Biomes.PLAINS, plains));

        assertEquals(1, warnings.size(), warnings.toString());
        assertTrue(warnings.getFirst().startsWith("Pack \"test\", config \"pack.yml\": attribute \"terra:biome/downfall\" skipped: "
                                                  + "vanilla parent \"minecraft:plains\": "), warnings.getFirst());
        assertNull(first.get(downfall));
    }

    @Test
    void readsAllocateNothing() {
        parents.beginLoad(CONTEXT);
        parents.prepare(PACK, Optional.of(fake(Map.of())), report);
        parents.prepare(pack("other", REGISTRY), Optional.empty(), report);
        TestBiome biome = new TestBiome("BIOME", 0, Biomes.DESERT.identifier());
        TestBiome plain = new TestBiome("PLAIN", 1, (PlatformBiome) () -> "plain");
        FakePackAttributes fake = new FakePackAttributes(HANDLE, parents,
            AttributeMap.builder().put(RESPAWN_ANCHOR.key(), AttributeEntry.override(true)).build(),
            Map.of("vanilla", RegistryKey.parse("minecraft:the_nether")),
            Map.of(biome, AttributeMap.builder().put(HAS_CEILING.key(), AttributeEntry.override(true)).build()));
        PackParents pack = parents.prepare(PACK, Optional.of(fake), report);
        biome.platform().setParentEntries(parents.biomeEntries(pack, Biomes.DESERT, value(Registries.BIOME, Biomes.DESERT)));
        FakePackAttributes unprepared = fake(Map.of());
        assertEquals(List.of(), warnings);
        assertEquals(1023, reads(fake, unprepared, biome, plain), "every read sees the expected entry or value");

        com.sun.management.ThreadMXBean threads = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();
        long thread = Thread.currentThread().threadId();
        int sink = 0;
        for(int i = 0; i < 100_000; i++) sink += reads(fake, unprepared, biome, plain);
        long before = threads.getThreadAllocatedBytes(thread);
        for(int i = 0; i < 10_000; i++) sink += reads(fake, unprepared, biome, plain);
        long allocated = threads.getThreadAllocatedBytes(thread) - before;

        assertEquals(110_000 * 1023, sink);
        assertTrue(allocated < 1024, allocated + " bytes allocated");
        System.out.println("readsAllocateNothing: " + allocated + " bytes over 10000 measured iterations");
    }

    private int reads(FakePackAttributes fake, FakePackAttributes unprepared, TestBiome biome, TestBiome plain) {
        int hash = 0;
        hash += parents.biomeOrNull(biome, PRECIPITATION) == null ? 0 : 1;
        hash += parents.biomeOrNull(plain, PRECIPITATION) == null ? 2 : 0;
        hash += parents.packOrNull(fake, HAS_CEILING) == null ? 0 : 4;
        hash += parents.packOrNull(fake, SKY_LIGHT) == null ? 0 : 8;
        hash += parents.packOrNull(unprepared, SKY_LIGHT) == null ? 16 : 0;
        hash += fake.resolve(PRECIPITATION, biome) ? 0 : 32;
        hash += fake.resolve(HAS_CEILING, plain) ? 64 : 0;
        hash += fake.resolve(HAS_CEILING, biome) ? 128 : 0;
        hash += fake.resolvePack(RESPAWN_ANCHOR) ? 256 : 0;
        hash += fake.resolvePack(HAS_CEILING) ? 512 : 0;
        return hash;
    }

    @Test
    void beginLoadAndClearPacksDropOldPacks() {
        parents.beginLoad(CONTEXT);
        FakePackAttributes fake = fake(Map.of());
        PackParents first = parents.prepare(PACK, Optional.of(fake), report);
        assertNotNull(parents.packOrNull(fake, HAS_CEILING));

        parents.beginLoad(CONTEXT);
        assertNull(parents.packOrNull(fake, HAS_CEILING));
        PackParents second = parents.prepare(PACK, Optional.of(fake), report);
        assertNotSame(first, second);
        assertEquals(AttributeEntry.override(false), parents.packOrNull(fake, HAS_CEILING));
        AttributeSlots desert = parents.biomeEntries(second, Biomes.DESERT, value(Registries.BIOME, Biomes.DESERT));

        parents.clearPacks();
        assertSame(CONTEXT, parents.context());
        assertNull(parents.packOrNull(fake, HAS_CEILING));
        FakePackAttributes reloaded = fake(Map.of());
        assertNull(parents.packOrNull(reloaded, HAS_CEILING));
        PackParents third = parents.prepare(PACK, Optional.of(reloaded), report);

        assertNotSame(second, third);
        assertSame(third, parents.prepare(PACK, Optional.of(reloaded), report));
        assertEquals(AttributeEntry.override(false), parents.packOrNull(reloaded, HAS_CEILING));
        assertNull(parents.packOrNull(fake, HAS_CEILING), "the old pack's attributes get no parents");
        AttributeSlots again = parents.biomeEntries(third, Biomes.DESERT, value(Registries.BIOME, Biomes.DESERT));
        assertNotSame(desert, again, "biome entries are read again for the new pack");
        assertEquals(AttributeEntry.override(false), again.get(PRECIPITATION));
        assertEquals(List.of(), warnings);
    }

    @Test
    void packReloadKeepsOldPacks() {
        parents.beginLoad(CONTEXT);
        FakePackAttributes fake = fake(Map.of());
        PackParents first = parents.prepare(PACK, Optional.of(fake), report);

        parents.beginPackReload();
        assertSame(CONTEXT, parents.context());
        assertEquals(AttributeEntry.override(false), parents.packOrNull(fake, HAS_CEILING), "the old pack keeps its parents");
        FakePackAttributes reloaded = fake(Map.of());
        PackParents second = parents.prepare(PACK, Optional.of(reloaded), report);
        assertNotSame(first, second);
        assertNotSame(first, parents.prepare(PACK, Optional.of(fake), report), "prepared fresh during the reload");
        assertEquals(AttributeEntry.override(false), parents.packOrNull(reloaded, HAS_CEILING));
        assertNotNull(parents.packOrNull(fake, HAS_CEILING));

        parents.endPackReload();
        assertSame(second, parents.prepare(PACK, Optional.of(reloaded), report));
        assertEquals(AttributeEntry.override(false), parents.packOrNull(reloaded, HAS_CEILING));
        assertNotNull(parents.packOrNull(fake, HAS_CEILING), "re-prepared during the reload, so kept");
        assertEquals(List.of(), warnings);

        parents.beginPackReload();
        parents.endPackReload();
        assertNull(parents.packOrNull(reloaded, HAS_CEILING), "not prepared again, so dropped");
    }

    @Test
    void unpreparedPackHasNoParents() {
        FakePackAttributes fake = fake(Map.of());
        assertNull(parents.context());
        assertNull(parents.packOrNull(fake, HAS_CEILING));
        assertThrows(IllegalStateException.class, () -> parents.prepare(PACK, Optional.of(fake), report));

        parents.beginLoad(CONTEXT);
        assertSame(CONTEXT, parents.context());
        assertNull(parents.packOrNull(fake, HAS_CEILING));
    }
}
