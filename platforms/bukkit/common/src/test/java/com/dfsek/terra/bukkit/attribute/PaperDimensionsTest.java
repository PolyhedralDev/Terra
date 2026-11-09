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
package com.dfsek.terra.bukkit.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TimelineTags;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;
import net.minecraft.world.timeline.Timelines;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.minecraft.attribute.FakePackAttributes;
import com.dfsek.terra.minecraft.attribute.MinecraftAttributeParents;
import com.dfsek.terra.minecraft.attribute.VanillaCodecs;
import com.dfsek.terra.minecraft.attribute.VanillaContext;
import com.dfsek.terra.world.attribute.platform.AttributeReport;

import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.HANDLE;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.REGISTRIES;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.attribute;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.registry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PaperDimensionsTest {
    private static final com.dfsek.terra.api.registry.Registry<Attribute<?>> REGISTRY = registry();
    private static final VanillaContext PAPER = PaperVanilla.context(REGISTRIES);
    private static final Map<String, RegistryKey> NETHER = Map.of("vanilla", RegistryKey.parse("minecraft:the_nether"));
    private static final Map<String, RegistryKey> OVERWORLD = Map.of("vanilla", RegistryKey.parse("minecraft:overworld"));
    private static final ResourceKey<Timeline> SEASONS = ResourceKey.create(Registries.TIMELINE, Identifier.parse("mypack:seasons"));

    private final List<String> warnings = new ArrayList<>();
    private final List<String> debugs = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, debugs::add);
    private final MinecraftAttributeParents parents = new MinecraftAttributeParents(HANDLE);
    private final List<String> worldInfos = new ArrayList<>();
    private final List<String> worldWarnings = new ArrayList<>();
    private final FakeServer server = new FakeServer();
    private boolean playersOnline;
    private final PaperDimensions dimensions = new PaperDimensions(HANDLE, parents, () -> playersOnline, server, worldInfos::add,
                                                                   worldWarnings::add);

    private static ConfigPack pack(@Nullable PackAttributes attributes) {
        Context context = new Context();
        if(attributes != null) context.put(Context.create(PackAttributes.class), attributes);
        return pack("test", () -> context);
    }

    private static ConfigPack pack(String id, Supplier<Context> context) {
        return (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getID" -> id;
                case "getRegistry" -> REGISTRY;
                case "getContext" -> context.get();
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                case "toString" -> "ConfigPack[" + id + "]";
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private FakePackAttributes attributes(Map<String, Object> entries, Map<String, RegistryKey> parentIds,
                                          List<Either<RegistryKey, com.dfsek.terra.api.registry.key.TagKey>> refs) {
        AttributeMap.Builder builder = AttributeMap.builder();
        entries.forEach((id, raw) -> put(builder, attribute(id), decoded(id, raw)));
        return new FakePackAttributes(HANDLE, parents, builder.build(), parentIds, Map.of(), refs, Map.of());
    }

    private FakePackAttributes attributes(Map<String, Object> entries, Map<String, RegistryKey> parentIds) {
        return attributes(entries, parentIds, List.of());
    }

    private static AttributeEntry<?> decoded(String id, Object raw) {
        try {
            return HANDLE.decodeEntry(attribute(id), raw, HANDLE.plainContext());
        } catch(DecodeException e) {
            throw new AssertionError(id + ": " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void put(AttributeMap.Builder builder, Attribute<T> attribute, AttributeEntry<?> entry) {
        builder.put(attribute.key(), (AttributeEntry<T>) entry);
    }

    private ConfigPack prepared(VanillaContext context, ConfigPack pack) {
        parents.beginLoad(context);
        parents.prepare(pack, HANDLE.pack(pack), report);
        return pack;
    }

    private ConfigPack prepared(@Nullable PackAttributes attributes) {
        return prepared(PAPER, pack(attributes));
    }

    private static DimensionType dimensionType(VanillaContext context, ResourceKey<DimensionType> key) {
        return context.dimensionTypes().getOrThrow(key).value();
    }

    private PaperDimensions.Built typeFor(VanillaContext context, ConfigPack pack, ResourceKey<DimensionType> world, String worldName) {
        return dimensions.typeFor(pack, world, dimensionType(context, world), worldName).orElseThrow();
    }

    private PaperDimensions.Built typeFor(ConfigPack pack, ResourceKey<DimensionType> world) {
        return typeFor(PAPER, pack, world, "world");
    }

    private static String notApplied(String key) {
        return notApplied(key, "world");
    }

    private static String notApplied(String key, String world) {
        return "Pack \"test\": attribute \"" + key + "\" is not applied to world \"" + world
               + "\": it is fixed when the world is created";
    }

    private static MappedRegistry<Timeline> timelines() {
        MappedRegistry<Timeline> timelines = new MappedRegistry<>(Registries.TIMELINE, Lifecycle.stable());
        REGISTRIES.lookupOrThrow(Registries.TIMELINE).listElements()
            .forEach(holder -> timelines.register(holder.key(), holder.value(), RegistrationInfo.BUILT_IN));
        RegistryOps<Object> ops = PAPER.ops();
        Timeline moon = REGISTRIES.lookupOrThrow(Registries.TIMELINE).getOrThrow(Timelines.MOON).value();
        timelines.register(SEASONS, reread(Timeline.DIRECT_CODEC, moon, ops, ops), RegistrationInfo.BUILT_IN);

        Map<TagKey<Timeline>, List<Holder<Timeline>>> tags = new HashMap<>();
        REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).listElements()
            .forEach(type -> type.value().timelines().unwrapKey().ifPresent(tag -> tags.put(tag, List.of())));
        tags.put(TimelineTags.IN_OVERWORLD, List.of(timelines.get(Timelines.OVERWORLD_DAY).orElseThrow(),
                                                    timelines.get(Timelines.MOON).orElseThrow(),
                                                    timelines.get(Timelines.VILLAGER_SCHEDULE).orElseThrow()));
        timelines.bindTags(tags);
        timelines.freeze();
        return timelines;
    }

    private static VanillaContext contextWith(MappedRegistry<Timeline> timelines) {
        Map<ResourceKey<?>, HolderLookup.RegistryLookup<?>> lookups = new LinkedHashMap<>();
        REGISTRIES.listRegistries().forEach(lookup -> lookups.put(lookup.key(), lookup));
        lookups.put(Registries.TIMELINE, timelines);
        RegistryOps<Object> ops = HolderLookup.Provider.create(lookups.values().stream()).createSerializationContext(JavaOps.INSTANCE);

        MappedRegistry<DimensionType> types = new MappedRegistry<>(Registries.DIMENSION_TYPE, Lifecycle.stable());
        REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).listElements().forEach(holder -> types.register(holder.key(),
            reread(DimensionType.DIRECT_CODEC, holder.value(), PAPER.ops(), ops), RegistrationInfo.BUILT_IN));
        types.freeze();
        lookups.put(Registries.DIMENSION_TYPE, types);
        return PaperVanilla.context(HolderLookup.Provider.create(lookups.values().stream()));
    }

    private static <T> T reread(Codec<T> codec, T value, RegistryOps<Object> from, RegistryOps<Object> to) {
        try {
            return VanillaCodecs.parse(codec, VanillaCodecs.encode(codec, value, from), to);
        } catch(DecodeException e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    private static Either<RegistryKey, com.dfsek.terra.api.registry.key.TagKey> id(String id) {
        return Either.left(RegistryKey.parse(id));
    }

    private static Either<RegistryKey, com.dfsek.terra.api.registry.key.TagKey> tag(String namespace, String id) {
        return Either.right(com.dfsek.terra.api.registry.key.TagKey.of(RegistryKey.of(namespace, id)));
    }

    private static List<ResourceKey<Timeline>> keys(DimensionType type) {
        return type.timelines().stream().map(holder -> holder.unwrapKey().orElseThrow()).toList();
    }

    @Test
    void pinnedFieldsFollowWorld() {
        ConfigPack pack = prepared(attributes(Map.of(), NETHER));

        PaperDimensions.Built built = typeFor(pack, BuiltinDimensionTypes.OVERWORLD);
        DimensionType type = built.type();

        assertEquals(Identifier.parse("terra:test/minecraft/overworld"), built.key());

        assertEquals(-64, type.minY());
        assertEquals(384, type.height());
        assertTrue(type.hasSkyLight());
        assertEquals(0.1f, type.ambientLight());
        assertTrue(type.hasCeiling());
        assertEquals(Optional.empty(), type.defaultClock());
        assertEquals(List.of(), warnings);
        assertEquals(List.of(notApplied("terra:dimension/min_y"), notApplied("terra:dimension/height"),
                             notApplied("terra:dimension/has_skylight")), debugs);
    }

    @Test
    void worldTypeIsDefaultParent() {
        ConfigPack pack = prepared(attributes(Map.of(), Map.of()));
        DimensionType nether = dimensionType(PAPER, BuiltinDimensionTypes.NETHER);

        PaperDimensions.Built inOverworld = typeFor(PAPER, pack, BuiltinDimensionTypes.OVERWORLD, "world");
        PaperDimensions.Built inNether = typeFor(PAPER, pack, BuiltinDimensionTypes.NETHER, "world_nether");

        assertNotSame(nether, inNether.type());
        assertEquals(nether, inNether.type());
        assertEquals(Identifier.parse("terra:test/minecraft/the_nether"), inNether.key());
        DimensionType overworld = dimensionType(PAPER, BuiltinDimensionTypes.OVERWORLD);
        assertNotSame(overworld, inOverworld.type());
        assertEquals(overworld, inOverworld.type());
        assertEquals(Identifier.parse("terra:test"), inOverworld.key());
        assertEquals(List.of(), warnings);
        assertEquals(List.of(), debugs);
    }

    @Test
    void directTimelinesFromRefs() {
        MappedRegistry<Timeline> timelines = timelines();
        VanillaContext context = contextWith(timelines);
        ConfigPack pack = prepared(context, pack(attributes(Map.of(), Map.of(), List.of(tag("minecraft", "in_overworld"),
                                                                                         id("mypack:seasons")))));

        dimensions.recordTimelines(pack, timelines);
        DimensionType type = typeFor(context, pack, BuiltinDimensionTypes.OVERWORLD, "world").type();

        assertEquals(List.of(Timelines.OVERWORLD_DAY, Timelines.MOON, Timelines.VILLAGER_SCHEDULE, SEASONS), keys(type));
        assertEquals(List.of(), warnings);
    }

    @Test
    void noValidRefKeepsParent() {
        MappedRegistry<Timeline> timelines = timelines();
        VanillaContext context = contextWith(timelines);
        ConfigPack pack = prepared(context, pack(attributes(Map.of(), Map.of(), List.of(id("mypack:nope")))));

        dimensions.recordTimelines(pack, timelines);
        DimensionType type = typeFor(context, pack, BuiltinDimensionTypes.OVERWORLD, "world").type();
        typeFor(context, pack, BuiltinDimensionTypes.OVERWORLD, "other");
        DimensionType nether = typeFor(context, pack, BuiltinDimensionTypes.NETHER, "world_nether").type();

        assertEquals(List.of(Timelines.OVERWORLD_DAY, Timelines.MOON, Timelines.VILLAGER_SCHEDULE), keys(type));
        assertEquals(keys(dimensionType(context, BuiltinDimensionTypes.NETHER)), keys(nether));
        assertEquals(List.of("Pack \"test\": schedule \"mypack:nope\" skipped: no such timeline",
                             "Pack \"test\", config \"pack.yml\": \"schedules\" ignored: no valid schedule; "
                             + "the parent's timelines are kept"),
                     warnings);
    }

    @Test
    void noAttributesLeavesWorld() {
        ConfigPack pack = prepared(null);

        assertEquals(Optional.empty(), dimensions.typeFor(pack, BuiltinDimensionTypes.OVERWORLD,
                                                          dimensionType(PAPER, BuiltinDimensionTypes.OVERWORLD), "world"));
        assertEquals(List.of(), warnings);
        assertEquals(List.of(), debugs);
    }

    @Test
    void unsupportedGenerationWarnsOnce() {
        ConfigPack pack = prepared(attributes(Map.of("terra:world/sea_level", 40, "terra:world/spawn_height", 80),
                                              Map.of("vanilla-generation", RegistryKey.parse("minecraft:nether"))));

        dimensions.registerPacks(List.of(pack));
        typeFor(PAPER, pack, BuiltinDimensionTypes.OVERWORLD, "world");
        typeFor(PAPER, pack, BuiltinDimensionTypes.NETHER, "world_nether");

        assertEquals(List.of("Pack \"test\", config \"pack.yml\": \"terra:world/sea_level\" ignored: not applied on this platform",
                             "Pack \"test\", config \"pack.yml\": \"terra:world/spawn_height\" ignored: not applied on this platform",
                             "Pack \"test\", config \"pack.yml\": \"vanilla-generation\" ignored: not applied on this platform"),
                     warnings);
    }

    @Test
    void logicalHeightCappedToWorld() {
        String reported = notApplied("terra:dimension/logical_height", "world_nether");
        PaperDimensions.Built inherited = typeFor(PAPER, prepared(attributes(Map.of(), OVERWORLD)), BuiltinDimensionTypes.NETHER,
                                                  "world_nether");

        assertEquals(256, inherited.type().logicalHeight());
        assertEquals(256, inherited.type().height());
        assertEquals(Identifier.parse("terra:test/minecraft/the_nether"), inherited.key());
        assertFalse(debugs.contains(reported), String.valueOf(debugs));

        ConfigPack set = prepared(attributes(Map.of("terra:dimension/logical_height", 384), OVERWORLD));
        typeFor(PAPER, set, BuiltinDimensionTypes.NETHER, "world_nether");
        assertEquals(256, typeFor(PAPER, set, BuiltinDimensionTypes.NETHER, "world_nether").type().logicalHeight());

        assertEquals(1, debugs.stream().filter(reported::equals).count(), String.valueOf(debugs));
        assertEquals(List.of(), warnings);
    }

    @Test
    void unreadablePackSkipped() {
        ConfigPack broken = pack("broken", () -> {
            throw new IllegalStateException("boom");
        });
        ConfigPack pack = prepared(attributes(Map.of("terra:world/sea_level", 40), Map.of()));

        dimensions.registerPacks(List.of(broken, pack));

        assertEquals(List.of("Pack \"broken\": timelines and dimension type skipped: its attributes could not be read"), worldWarnings);
        assertEquals(List.of("Pack \"test\", config \"pack.yml\": \"terra:world/sea_level\" ignored: not applied on this platform"),
                     warnings);
    }

    private static World world() {
        return (World) Proxy.newProxyInstance(World.class.getClassLoader(), new Class<?>[]{ World.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getName" -> "world";
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                case "toString" -> "World[world]";
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private BukkitChunkGeneratorWrapper initialise() {
        BukkitChunkGeneratorWrapper wrapper = new BukkitChunkGeneratorWrapper(null, prepared(attributes(
            Map.of("terra:world/mob_generation", false), OVERWORLD)), null);
        dimensions.onWorldInit(world(), wrapper);
        return wrapper;
    }

    private static final Identifier TERRA_TEST = Identifier.parse("terra:test");
    private static final String KEEPS = "Pack \"test\": world \"world\" keeps its dimension type";

    @ParameterizedTest(name = "registered {0}, players online {1}")
    @CsvSource({
        "false, false, true,  true,  false",
        "true,  false, false, true,  false",
        "false, true,  false, false, true",
        "true,  true,  false, true,  false"
    })
    void worldInitRegistersThenApplies(boolean registered, boolean online, boolean registers, boolean applies, boolean keeps) {
        if(registered) server.registered.add(TERRA_TEST);
        playersOnline = online;

        assertFalse(initialise().shouldGenerateMobs());

        assertEquals(registers ? List.of(TERRA_TEST) : List.of(), server.registrations);
        assertEquals(applies ? List.of(TERRA_TEST) : List.of(), server.applications);
        assertEquals(keeps ? List.of(KEEPS + " until the server restarts, since players are online") : List.of(), worldInfos);
        assertEquals(List.of(), worldWarnings);
    }

    @ParameterizedTest(name = "{0} fails")
    @CsvSource(delimiter = '|', quoteCharacter = '"', value = {
        "lookup   | false | : the pack's could not be registered",
        "register | false | : the pack's could not be registered",
        "apply    | true  | : wyck could not apply the pack's"
    })
    void failedStepKeepsTheWorldsType(String step, boolean registers, String warning) {
        IllegalStateException boom = new IllegalStateException("boom");
        switch(step) {
            case "lookup" -> server.lookupFailure = boom;
            case "register" -> server.registerFailure = boom;
            case "apply" -> server.applyFailure = boom;
            default -> throw new IllegalArgumentException(step);
        }

        assertFalse(initialise().shouldGenerateMobs());

        assertEquals(registers ? List.of(TERRA_TEST) : List.of(), server.registrations);
        assertEquals(List.of(), server.applications);
        assertEquals(List.of(KEEPS + warning), worldWarnings);
    }


    private static final class FakeServer implements PaperDimensions.Server {
        private final Set<Identifier> registered = new HashSet<>();
        private final List<Identifier> registrations = new ArrayList<>();
        private final List<Identifier> applications = new ArrayList<>();
        private @Nullable RuntimeException lookupFailure;
        private @Nullable RuntimeException registerFailure;
        private @Nullable RuntimeException applyFailure;

        @Override
        public void registerTimelines(Consumer<WritableRegistry<Timeline>> registration) {
            registration.accept(new MappedRegistry<>(Registries.TIMELINE, Lifecycle.stable()));
        }

        @Override
        public Registry<Timeline> timelines() {
            return PaperDimensionsTest.timelines();
        }

        @Override
        public Holder<DimensionType> dimensionType(World world) {
            return REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.OVERWORLD);
        }

        @Override
        public boolean isRegistered(Identifier key) {
            if(lookupFailure != null) throw lookupFailure;
            return registered.contains(key);
        }

        @Override
        public void registerDimension(Identifier key, DimensionType type) {
            if(registerFailure != null) throw registerFailure;
            registrations.add(key);
            registered.add(key);
        }

        @Override
        public void applyDimension(World world, Identifier key) {
            if(applyFailure != null) throw applyFailure;
            applications.add(key);
        }
    }
}
