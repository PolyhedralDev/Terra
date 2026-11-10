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

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.minecraft.attribute.MinecraftAttributeParents;
import com.dfsek.terra.minecraft.attribute.MinecraftDimensionFactory;
import com.dfsek.terra.minecraft.attribute.MinecraftTimelines;
import com.dfsek.terra.minecraft.attribute.PackAttributeTable;
import com.dfsek.terra.minecraft.attribute.PackParents;
import com.dfsek.terra.minecraft.attribute.TimelineRef;
import com.dfsek.terra.minecraft.attribute.VanillaCodecs;
import com.dfsek.terra.minecraft.attribute.VanillaContext;
import com.dfsek.terra.minecraft.attribute.WorldSettings;
import com.dfsek.terra.world.attribute.platform.AttributeReport;


public final class PaperDimensions {
    // Paper fixes these when it builds the level.
    static final List<String> LEVEL_FIXED = List.of("min_y", "height", "has_skylight");

    private static final Logger LOGGER = LoggerFactory.getLogger(PaperDimensions.class);
    private static final String PACK_CONFIG = "pack.yml";
    private static final String NOT_ON_THIS_PLATFORM = "not applied on this platform";
    private static final String VANILLA_GENERATION = "vanilla-generation";
    private static final String HEIGHT = "height";
    private static final String LOGICAL_HEIGHT = "logical_height";
    private static final List<AttributeKey<?>> UNSUPPORTED_GENERATION = List.of(AttributeKey.parse("terra:world/sea_level"),
                                                                                 AttributeKey.parse("terra:world/spawn_height"));

    private final AttributeHandle handle;
    private final MinecraftAttributeParents parents;
    private final BooleanSupplier playersOnline;
    private final Server server;
    private final Consumer<String> info;
    private final Consumer<String> warn;
    // Direct lists survive tag reloads.
    private final Map<String, TimelineRef.Direct> resolvedTimelines = new ConcurrentHashMap<>();

    public PaperDimensions(AttributeHandle handle, MinecraftAttributeParents parents, BooleanSupplier playersOnline) {
        this(handle, parents, playersOnline, new PaperServer(), LOGGER::info, LOGGER::warn);
    }

    PaperDimensions(AttributeHandle handle, MinecraftAttributeParents parents, BooleanSupplier playersOnline, Server server,
                    Consumer<String> info, Consumer<String> warn) {
        this.handle = Objects.requireNonNull(handle, "handle");
        this.parents = Objects.requireNonNull(parents, "parents");
        this.playersOnline = Objects.requireNonNull(playersOnline, "playersOnline");
        this.server = Objects.requireNonNull(server, "server");
        this.info = Objects.requireNonNull(info, "info");
        this.warn = Objects.requireNonNull(warn, "warn");
    }

    public void registerPacks(Iterable<ConfigPack> packs) {
        VanillaContext context = context();
        for(ConfigPack pack : packs) {
            try {
                registerPack(pack, context);
            } catch(RuntimeException e) {
                LOGGER.debug("Failed to read the attributes of pack {}", pack.getID(), e);
                warn.accept("Pack \"" + pack.getID() + "\": timelines and dimension type skipped: its attributes could not be read");
            }
        }
    }

    private void registerPack(ConfigPack pack, VanillaContext context) {
        Optional<PackAttributes> attributes = handle.pack(pack);
        if(attributes.isEmpty()) return;
        PackParents packParents = prepare(pack, attributes);
        warnUnsupportedGeneration(attributes.get(), packParents.report());

        if(!attributes.get().scheduleDefinitions().isEmpty()) {
            try {
                server.registerTimelines(registry -> MinecraftTimelines.register(pack, packParents, registry, handle, context));
            } catch(RuntimeException e) {
                LOGGER.debug("Wyck failed to register the timelines of pack {}", pack.getID(), e);
                packParents.report().ignored(PACK_CONFIG, "schedule-definitions", "wyck could not register the timelines");
            }
        }
        try {
            recordTimelines(pack, server.timelines());
        } catch(RuntimeException e) {
            LOGGER.debug("Failed to resolve the schedules of pack {}", pack.getID(), e);
            packParents.report().ignored(PACK_CONFIG, "schedules", "the timelines could not be resolved; the parent's are kept");
        }
    }

    private static void warnUnsupportedGeneration(PackAttributes attributes, AttributeReport report) {
        for(AttributeKey<?> key : UNSUPPORTED_GENERATION) {
            if(attributes.entries().get(key).isPresent()) report.ignored(PACK_CONFIG, key.toString(), NOT_ON_THIS_PLATFORM);
        }
        if(attributes.parents().containsKey(VANILLA_GENERATION)) report.ignored(PACK_CONFIG, VANILLA_GENERATION, NOT_ON_THIS_PLATFORM);
    }

    void recordTimelines(ConfigPack pack, Registry<Timeline> registry) {
        PackParents packParents = prepare(pack, handle.pack(pack));
        MinecraftTimelines.Pending pending = MinecraftTimelines.pending(pack, packParents);
        if(pending == null) {
            resolvedTimelines.remove(pack.getID());
            return;
        }
        MinecraftTimelines.Pending named = new MinecraftTimelines.Pending(pending.report(), pending.refs(), HolderSet.empty());
        List<Holder<Timeline>> contents = MinecraftTimelines.contents(named,
            tag -> registry.get(tag).map(set -> set.stream().toList()).orElse(null), registry);
        if(contents.isEmpty()) {
            resolvedTimelines.remove(pack.getID());
            return;
        }
        List<ResourceKey<Timeline>> keys = contents.stream().flatMap(holder -> holder.unwrapKey().stream()).toList();
        resolvedTimelines.put(pack.getID(), new TimelineRef.Direct(keys));
    }

    public Optional<Built> typeFor(ConfigPack pack, ResourceKey<DimensionType> worldTypeKey, DimensionType worldType, String worldName) {
        Optional<PackAttributes> attributes = handle.pack(pack);
        if(attributes.isEmpty()) return Optional.empty();
        VanillaContext context = context();
        PackParents packParents = parents.prepare(pack, attributes, AttributeReport.logging(pack), worldTypeKey);
        MinecraftDimensionFactory factory = new MinecraftDimensionFactory(handle, context);
        DimensionType type = factory.create(packParents, resolvedTimelines.get(pack.getID()));

        Map<?, ?> encoded = (Map<?, ?>) VanillaCodecs.encode(DimensionType.DIRECT_CODEC, type, context.ops());
        Map<?, ?> world = (Map<?, ?>) VanillaCodecs.encode(DimensionType.DIRECT_CODEC, worldType, context.ops());
        Map<Object, Object> pinned = new LinkedHashMap<>(encoded);
        for(String field : LEVEL_FIXED) pinned.put(field, world.get(field));
        reportPinned(packParents, world, worldName);
        if(pinned.get(LOGICAL_HEIGHT) instanceof Number logical && world.get(HEIGHT) instanceof Number height
           && logical.intValue() > height.intValue()) {
            pinned.put(LOGICAL_HEIGHT, height);
            reportLogicalHeight(packParents, attributes.get(), height, worldName);
        }

        Identifier key = MinecraftDimensionFactory.typeId(pack, worldTypeKey, !pinned.equals(encoded));
        if(!pinned.equals(encoded)) {
            try {
                type = VanillaCodecs.parse(DimensionType.DIRECT_CODEC, pinned, context.ops());
            } catch(DecodeException e) {
                throw new IllegalStateException("the dimension type with the world's fields was rejected: " + e.getMessage(), e);
            }
        }
        return Optional.of(new Built(key, type, factory.worldSettings(packParents, type)));
    }

    private void reportPinned(PackParents packParents, Map<?, ?> world, String worldName) {
        for(String field : LEVEL_FIXED) {
            for(PackAttributeTable.Field bound : packParents.table().dimension()) {
                if(!bound.name().equals(field)) continue;
                Object value = resolveEncoded(bound.attribute(), packParents);
                if(value != null && !sameValue(value, world.get(field))) {
                    packParents.report().notAppliedToWorld(bound.attribute().getRegistryKey().toString(), worldName);
                }
            }
        }
    }

    private void reportLogicalHeight(PackParents packParents, PackAttributes attributes, Number height, String worldName) {
        for(PackAttributeTable.Field bound : packParents.table().dimension()) {
            if(!bound.name().equals(LOGICAL_HEIGHT) || attributes.entries().get(bound.attribute().key()).isEmpty()) continue;
            if(resolveEncoded(bound.attribute(), packParents) instanceof Number value && value.intValue() > height.intValue()) {
                packParents.report().notAppliedToWorld(bound.attribute().getRegistryKey().toString(), worldName);
            }
        }
    }

    private <T> @Nullable Object resolveEncoded(Attribute<T> attribute, PackParents packParents) {
        T value = packParents.resolvePack(handle, attribute);
        return value == null ? null : attribute.type().encode(value);
    }

    private static boolean sameValue(Object value, @Nullable Object other) {
        if(value instanceof Number number && other instanceof Number otherNumber) {
            return number.doubleValue() == otherNumber.doubleValue();
        }
        return value.equals(other);
    }

    /**
     * A new type isn't registered while players are online: their clients already hold the registries.
     */
    public void onWorldInit(World world, BukkitChunkGeneratorWrapper wrapper) {
        ConfigPack pack = wrapper.bind(world);
        String keeps = "Pack \"" + pack.getID() + "\": world \"" + world.getName() + "\" keeps its dimension type";
        Built built;
        try {
            Holder<DimensionType> current = server.dimensionType(world);
            ResourceKey<DimensionType> currentKey = current.unwrapKey()
                .orElseThrow(() -> new IllegalStateException("the world's dimension type has no key"));
            Optional<Built> typed = typeFor(pack, currentKey, current.value(), world.getName());
            if(typed.isEmpty()) return;
            built = typed.get();
        } catch(RuntimeException e) {
            LOGGER.debug("Failed to build the dimension type of pack {} for world {}", pack.getID(), world.getName(), e);
            warn.accept(keeps + ": the pack's could not be built");
            return;
        }
        wrapper.setMobGeneration(built.settings().mobGeneration());

        try {
            boolean registered = server.isRegistered(built.key());
            boolean online = playersOnline.getAsBoolean();
            if(shouldRegister(registered, online)) {
                server.registerDimension(built.key(), built.type());
            } else if(!registered) {
                info.accept(keeps + " until the server restarts, since players are online");
                return;
            }
        } catch(RuntimeException e) {
            LOGGER.debug("Failed to register dimension type {}", built.key(), e);
            warn.accept(keeps + ": the pack's could not be registered");
            return;
        }
        try {
            server.applyDimension(world, built.key());
        } catch(RuntimeException e) {
            LOGGER.debug("Wyck failed to give world {} dimension type {}", world.getName(), built.key(), e);
            warn.accept(keeps + ": wyck could not apply the pack's");
        }
    }

    static boolean shouldRegister(boolean registered, boolean playersOnline) {
        return !registered && !playersOnline;
    }

    private VanillaContext context() {
        return parents.requireContext();
    }

    private PackParents prepare(ConfigPack pack, Optional<PackAttributes> attributes) {
        return parents.prepare(pack, attributes, AttributeReport.logging(pack));
    }


    public record Built(Identifier key, DimensionType type, WorldSettings settings) { }


    interface Server {
        void registerTimelines(Consumer<WritableRegistry<Timeline>> registration);

        Registry<Timeline> timelines();

        Holder<DimensionType> dimensionType(World world);

        boolean isRegistered(Identifier key);

        void registerDimension(Identifier key, DimensionType type);

        void applyDimension(World world, Identifier key);
    }


    private static final class PaperServer implements Server {
        @Override
        public void registerTimelines(Consumer<WritableRegistry<Timeline>> registration) {
            WyckBridge.registerTimelines(registration);
        }

        @Override
        public Registry<Timeline> timelines() {
            return PaperVanilla.server().registryAccess().lookupOrThrow(Registries.TIMELINE);
        }

        @Override
        public Holder<DimensionType> dimensionType(World world) {
            return PaperVanilla.level(world).dimensionTypeRegistration();
        }

        @Override
        public boolean isRegistered(Identifier key) {
            return PaperVanilla.server().registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE).containsKey(key);
        }

        @Override
        public void registerDimension(Identifier key, DimensionType type) {
            WyckBridge.registerDimension(key, type);
        }

        @Override
        public void applyDimension(World world, Identifier key) {
            WyckBridge.applyDimension(world, key);
            ServerLevel level = PaperVanilla.level(world);
            level.setEnvironmentAttributes(EnvironmentAttributeSystem.builder().addDefaultLayers(level).build());
            level.updateSkyBrightness();
        }
    }
}
