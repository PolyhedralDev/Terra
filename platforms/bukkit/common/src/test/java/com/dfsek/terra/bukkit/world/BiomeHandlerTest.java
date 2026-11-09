/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.dfsek.terra.bukkit.world;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.minecraft.attribute.BiomeTags.PlatformTagSource;
import com.dfsek.terra.minecraft.attribute.MinecraftBiomeFactory;
import com.dfsek.terra.world.attribute.platform.AttributeReport;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;

import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.REGISTRIES;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;


class BiomeHandlerTest {
    private static final Biome PARENT = biome(Biomes.DESERT);
    private static final Biome BUILT = biome(Biomes.PLAINS);
    private static final String PREFIX = "Pack \"test\", config \"BIOME\": ";

    private final List<String> warnings = new ArrayList<>();

    private static Biome biome(net.minecraft.resources.ResourceKey<Biome> key) {
        return REGISTRIES.lookupOrThrow(Registries.BIOME).getOrThrow(key).value();
    }

    private BiomeHandler.Registration<Biome> register(MinecraftBiomeFactory.Built built, Function<Biome, Biome> wyck) {
        return BiomeHandler.registration(() -> {
            if(built == null) throw new IllegalStateException("boom");
            return built;
        }, wyck, PARENT, VillagerType.DESERT, "test", "BIOME", warnings::add);
    }

    private static Biome rejecting(Biome biome, Biome rejected) {
        if(biome == rejected) throw new IllegalArgumentException("boom");
        return biome;
    }

    private static Stream<Arguments> registrations() {
        Function<Biome, Biome> readsAll = b -> b;
        Function<Biome, Biome> rejectsBuilt = b -> rejecting(b, BUILT);
        Function<Biome, Biome> rejectsAll = b -> rejecting(b, b);
        return Stream.of(
            Arguments.of(true, named("wyck reads all", readsAll), BUILT, VillagerType.SNOW, null),
            Arguments.of(false, named("wyck reads all", readsAll), PARENT, VillagerType.DESERT,
                "biome built from its parent: its attributes could not be applied"),
            Arguments.of(true, named("wyck rejects the built biome", rejectsBuilt), PARENT, VillagerType.DESERT,
                "biome built from its parent: wyck could not read its attributes"),
            Arguments.of(true, named("wyck rejects all", rejectsAll), null, null,
                "biome skipped: wyck could not read its attributes or its parent"),
            Arguments.of(false, named("wyck rejects all", rejectsAll), null, null,
                "biome skipped: its attributes could not be applied, and wyck could not read its parent"));
    }

    @ParameterizedTest(name = "builds {0}, {1}")
    @MethodSource("registrations")
    void registrationFallsBackToTheParent(boolean builds, Function<Biome, Biome> wyck, @Nullable Biome biome,
                                          @Nullable ResourceKey<VillagerType> villagerType, @Nullable String warning) {
        BiomeHandler.Registration<Biome> registration = register(builds ? new MinecraftBiomeFactory.Built(BUILT, VillagerType.SNOW)
                                                                        : null, wyck);

        if(biome == null) {
            assertNull(registration);
        } else {
            assertSame(biome, registration.biome());
            assertEquals(villagerType, registration.villagerType());
        }
        assertEquals(warning == null ? List.of() : List.of(PREFIX + warning), warnings);
    }

    @Test
    void failedAppendSkipsTag() {
        TagKey<Biome> isForest = TagKey.create(Registries.BIOME, Identifier.withDefaultNamespace("is_forest"));
        TagKey<Biome> isOcean = TagKey.create(Registries.BIOME, Identifier.withDefaultNamespace("is_ocean"));
        TagKey<Biome> hasSnow = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("mypack", "has_snow"));
        List<Identifier> terra = List.of(Identifier.fromNamespaceAndPath("terra", "t/b"));
        Map<TagKey<Biome>, List<Identifier>> additions = new LinkedHashMap<>();
        additions.put(isForest, terra);
        additions.put(isOcean, terra);
        additions.put(hasSnow, terra);
        List<TagKey<Biome>> appended = new ArrayList<>();

        int count = BiomeHandler.appendAll(additions, (tag, biomes) -> {
            if(tag == isOcean) throw new IllegalStateException("boom");
            appended.add(tag);
        }, "wyck could not append to the tag", warnings::add);

        assertEquals(2, count);
        assertEquals(List.of(isForest, hasSnow), appended);
        assertEquals(List.of("Biome tag \"minecraft:is_ocean\": terra biomes not added: wyck could not append to the tag"), warnings);
    }

    @Test
    void nothingNewSkipsRegistration() {
        List<String> info = new ArrayList<>();
        List<List<String>> registered = new ArrayList<>();

        BiomeHandler.registerNew(List.<String>of(), registered::add, info::add);
        BiomeHandler.registerNew(List.of("a", "b"), registered::add, info::add);

        assertEquals(List.of(List.of("a", "b")), registered);
        assertEquals(List.of("No new custom biomes to register.", "Registering 2 custom biomes through Wyck..."), info);
    }

    private static ConfigPack pack(String id) {
        return (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getID" -> id;
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                case "toString" -> "ConfigPack[" + id + "]";
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static final Identifier PLAINS = Biomes.PLAINS.identifier();
    private static final Identifier DESERT = Biomes.DESERT.identifier();

    private PlatformTagSource tags(String pack, String config) {
        return new PlatformTagSource(new AttributeReport(pack, warnings::add, m -> { }), config, Set.of());
    }

    private void add(BiomeHandler.Run<String> run, String pack, String config, String key, Identifier parent, boolean registered,
                     net.minecraft.resources.ResourceKey<VillagerType> type, List<String> after) {
        run.add(Identifier.fromNamespaceAndPath("terra", key), parent, registered,
            () -> new BiomeHandler.Registration<>(pack + "/" + config, type), tags(pack, config), () -> after.add(pack + "/" + config),
            pack, config, warnings::add);
    }

    @Test
    void unreadablePackSkipped() {
        List<String> after = new ArrayList<>();

        BiomeHandler.Run<String> run = BiomeHandler.<String, String>collect(
            List.of(pack("first"), pack("unreadable"), pack("broken"), pack("last")), pack -> {
                if(pack.getID().equals("unreadable")) throw new ClassCastException("boom");
                return pack.getID();
            }, (pack, prepared, packRun) -> {
                add(packRun, prepared, "a", prepared + "/a", PLAINS, false, VillagerType.PLAINS, after);
                if(prepared.equals("broken")) throw new IllegalArgumentException("boom");
            }, warnings::add);
        run.afterRegistration.forEach(Runnable::run);

        assertEquals(List.of("Pack \"unreadable\": biomes skipped: its attributes could not be read",
            "Pack \"broken\": biomes skipped: they could not be built"), warnings);
        assertEquals(List.of("first/a", "last/a"), run.toRegister);
        assertEquals(List.of(Identifier.parse("terra:first/a"), Identifier.parse("terra:last/a")),
            List.copyOf(run.villagerTypes.keySet()));
        assertEquals(Map.of(PLAINS, List.of(Identifier.parse("terra:first/a"), Identifier.parse("terra:last/a"))), run.byParent);
        assertEquals(Set.of(Identifier.parse("terra:first/a"), Identifier.parse("terra:last/a")), run.platformTags.keySet());
        assertEquals(List.of("first/a", "last/a"), after);
    }

    @Test
    void registeredBiomeNotQueued() {
        List<String> after = new ArrayList<>();
        BiomeHandler.Run<String> run = new BiomeHandler.Run<>(null);

        add(run, "test", "a", "test/a", PLAINS, true, VillagerType.PLAINS, after);

        assertEquals(List.of(), run.toRegister);
        assertEquals(Map.of(), run.villagerTypes);
        assertEquals(Map.of(PLAINS, List.of(Identifier.parse("terra:test/a"))), run.byParent);
        assertEquals(1, run.afterRegistration.size());
    }

    @Test
    void sameKeyIsQueuedOnce() {
        List<String> after = new ArrayList<>();

        BiomeHandler.Run<String> run = BiomeHandler.<ConfigPack, String>collect(List.of(pack("test"), pack("TEST")), pack -> pack,
            (pack, prepared, packRun) -> {
                if(pack.getID().equals("test")) {
                    add(packRun, "test", "Snowy", "test/snowy", PLAINS, false, VillagerType.SNOW, after);
                    add(packRun, "test", "SNOWY", "test/snowy", DESERT, false, VillagerType.DESERT, after);
                } else {
                    add(packRun, "TEST", "snowy", "test/snowy", DESERT, false, VillagerType.DESERT, after);
                }
            }, warnings::add);
        run.afterRegistration.forEach(Runnable::run);

        Identifier snowy = Identifier.parse("terra:test/snowy");
        assertEquals(List.of("test/Snowy"), run.toRegister);
        assertEquals(Map.of(snowy, VillagerType.SNOW), run.villagerTypes);
        assertEquals(Map.of(PLAINS, List.of(snowy)), run.byParent);
        assertEquals("Snowy", run.platformTags.get(snowy).config());
        assertEquals(List.of("test/Snowy", "test/SNOWY", "TEST/snowy"), after);
        assertEquals(List.of(
            "Pack \"test\", config \"SNOWY\": biome \"terra:test/snowy\" shared with another config: their IDs differ only in case",
            "Pack \"TEST\", config \"snowy\": biome \"terra:test/snowy\" shared with another config: their IDs differ only in case"),
            warnings);
    }

    @Test
    void unreadableParentEntriesAreEmpty() {
        AttributeSlots entries = BiomeHandler.parentEntries(() -> {
            throw new ClassCastException("boom");
        }, "test", "BIOME", warnings::add);

        assertSame(AttributeSlots.EMPTY, entries);
        assertEquals(List.of(PREFIX + "parent attributes ignored: they could not be read"), warnings);
    }

    private static TagKey<Biome> tag(String id) {
        return TagKey.create(Registries.BIOME, Identifier.parse(id));
    }

    private static Optional<Holder<Biome>> lookup(Identifier id) {
        return REGISTRIES.lookupOrThrow(Registries.BIOME).get(net.minecraft.resources.ResourceKey.create(Registries.BIOME, id))
            .map(h -> h);
    }

    @Test
    void reloadedTagsGetTerraBiomesOnce() {
        Identifier plains = Biomes.PLAINS.identifier();
        Identifier forest = Biomes.FOREST.identifier();
        Identifier terra = Biomes.DESERT.identifier();
        TagKey<Biome> villagePlains = tag("minecraft:has_structure/village_plains");
        TagKey<Biome> isForest = tag("minecraft:is_forest");
        TagKey<Biome> hasSnow = tag("mypack:has_snow");
        TagKey<Biome> isOcean = tag("minecraft:is_ocean");
        Map<TagKey<Biome>, List<Identifier>> tags = new LinkedHashMap<>();
        tags.put(villagePlains, List.of(plains));
        tags.put(isForest, List.of(forest));
        tags.put(isOcean, List.of(Biomes.OCEAN.identifier()));
        Set<RegistryKey> platformTags = new LinkedHashSet<>(List.of(RegistryKey.parse("mypack:has_snow"),
            RegistryKey.parse("minecraft:is_forest")));
        PlatformTagSource source = new PlatformTagSource(new AttributeReport("test", warnings::add, m -> { }), "BIOME", platformTags);

        Map<TagKey<Biome>, List<Identifier>> additions = BiomeHandler.reloadAdditions(tags, BiomeHandlerTest::lookup,
            Map.of(plains, List.of(terra)), Map.of(terra, source));

        assertEquals(List.of(villagePlains, isForest, hasSnow), List.copyOf(additions.keySet()));
        assertEquals(List.of(terra), additions.get(villagePlains));
        assertEquals(List.of(terra), additions.get(isForest));
        assertEquals(List.of(terra), additions.get(hasSnow));
        assertEquals(List.of(), warnings);

        Map<TagKey<Biome>, List<Identifier>> reloaded = new LinkedHashMap<>(tags);
        additions.forEach((tag, ids) -> reloaded.merge(tag, ids, (old, added) -> Stream.concat(old.stream(), added.stream()).toList()));
        assertEquals(Map.of(), BiomeHandler.reloadAdditions(reloaded, BiomeHandlerTest::lookup, Map.of(plains, List.of(terra)),
            Map.of(terra, source)));
    }
}
