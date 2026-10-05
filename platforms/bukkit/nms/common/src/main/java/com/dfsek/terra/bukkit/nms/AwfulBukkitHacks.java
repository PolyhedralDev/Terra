package com.dfsek.terra.bukkit.nms;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.commands.arguments.blocks.BlockStateParser.BlockResult;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntitySnapshot;
import org.bukkit.craftbukkit.generator.CraftLimitedRegion;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.api.entity.EntityTypeExtended;
import com.dfsek.terra.bukkit.world.BukkitBiomeInfo;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.bukkit.nms.config.VanillaBiomeProperties;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityTypeExtended;
import com.dfsek.terra.registry.master.ConfigRegistry;


public class AwfulBukkitHacks {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwfulBukkitHacks.class);

    private static final Map<Identifier, List<Identifier>> terraBiomeMap = new HashMap<>();

    public static void registerBiomes(ConfigRegistry configRegistry, NMSVersionBindings bindings) {
        try {
            LOGGER.info("Hacking biome registry...");
            MappedRegistry<Biome> biomeRegistry = (MappedRegistry<Biome>) RegistryFetcher.biomeRegistry();

            // Unfreeze the biome registry to allow modification
            Reflection.MAPPED_REGISTRY.setFrozen(biomeRegistry, false);

            // Register the terra biomes to the registry
            configRegistry.forEach(pack -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((key, biome) -> {
                try {
                    BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();

                    NamespacedKey vanillaBukkitKey = platformBiome.getHandle().getKey();
                    Identifier vanillaMinecraftKey = Identifier.fromNamespaceAndPath(vanillaBukkitKey.getNamespace(),
                        vanillaBukkitKey.getKey());

                    VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);

                    Biome platform = NMSBiomeInjector.createBiome(biomeRegistry.get(vanillaMinecraftKey).orElseThrow().value(),
                        vanillaBiomeProperties, bindings);

                    Identifier delegateMinecraftKey = Identifier.fromNamespaceAndPath("terra",
                        NMSBiomeInjector.createBiomeID(pack, key));
                    NamespacedKey delegateBukkitKey = NamespacedKey.fromString(delegateMinecraftKey.toString());
                    ResourceKey<Biome> delegateKey = ResourceKey.create(Registries.BIOME, delegateMinecraftKey);

                    Reference<Biome> holder = biomeRegistry.register(delegateKey, platform, RegistrationInfo.BUILT_IN);
                    Reflection.REFERENCE.invokeBindValue(holder, platform); // IMPORTANT: bind holder.

                    platformBiome.getContext().put(new BukkitBiomeInfo(delegateBukkitKey));
                    platformBiome.getContext().put(new NMSBiomeInfo(delegateKey));

                    Map<ResourceKey<Biome>, ResourceKey<VillagerType>> villagerMap = Reflection.VILLAGER_TYPE.getByBiome();

                    villagerMap.put(delegateKey,
                        Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(),
                            villagerMap.getOrDefault(delegateKey, VillagerType.PLAINS)));

                    terraBiomeMap.computeIfAbsent(vanillaMinecraftKey, i -> new ArrayList<>()).add(delegateKey.identifier());

                    LOGGER.debug("Registered biome: " + delegateKey);
                } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }));

            Reflection.MAPPED_REGISTRY.setFrozen(biomeRegistry, true); // freeze registry again :)

            LOGGER.info("Doing tag garbage....");
            Map<TagKey<Biome>, List<Holder<Biome>>> collect = biomeRegistry
                .getTags() // streamKeysAndEntries
                .collect(HashMap::new,
                    (map, pair) ->
                        map.put(pair.key(), new ArrayList<>(Reflection.HOLDER_SET.invokeContents(pair).stream().toList())),
                    HashMap::putAll);

            terraBiomeMap
                .forEach((vb, terraBiomes) ->
                    NMSBiomeInjector.getEntry(biomeRegistry, vb).ifPresentOrElse(
                        vanilla -> terraBiomes.forEach(
                            tb -> NMSBiomeInjector.getEntry(biomeRegistry, tb).ifPresentOrElse(
                                terra -> {
                                    LOGGER.debug("{} (vanilla for {}): {}",
                                        vanilla.unwrapKey().orElseThrow().identifier(),
                                        terra.unwrapKey().orElseThrow().identifier(),
                                        vanilla.tags().toList());
                                    vanilla.tags()
                                        .forEach(tag -> collect
                                            .computeIfAbsent(tag, t -> new ArrayList<>())
                                            .add(terra));
                                },
                                () -> LOGGER.error("No such biome: {}", tb))),
                        () -> LOGGER.error("No vanilla biome: {}", vb)));

            resetTags(biomeRegistry);
            bindTags(biomeRegistry, collect);

        } catch(SecurityException | IllegalArgumentException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static <T> void bindTags(MappedRegistry<T> registry, Map<TagKey<T>, List<Holder<T>>> tagEntries) {
        Map<Reference<T>, List<TagKey<T>>> map = new IdentityHashMap<>();
        Reflection.MAPPED_REGISTRY.getByKey(registry).values().forEach(entry -> map.put(entry, new ArrayList<>()));
        tagEntries.forEach((tag, entries) -> {
            for(Holder<T> holder : entries) {
                //                if (!holder.canSerializeIn(registry.asLookup())) {
                //                    throw new IllegalStateException("Can't create named set " + tag + " containing value " + holder + "
                //                    from outside registry " + this);
                //                }

                if(!(holder instanceof Holder.Reference<T> reference)) {
                    throw new IllegalStateException("Found direct holder " + holder + " value in tag " + tag);
                }

                map.get(reference).add(tag);
            }
        });
        //        Set<TagKey<T>> set = Sets.difference(registry.tags.keySet(), tagEntries.keySet());
        //        if (!set.isEmpty()) {
        //            LOGGER.warn(
        //                "Not all defined tags for registry {} are present in data pack: {}",
        //                registry.key(),
        //                set.stream().map(tag -> tag.location().toString()).sorted().collect(Collectors.joining(", "))
        //            );
        //        }

        Map<TagKey<T>, Named<T>> map2 = new IdentityHashMap<>(registry.getTags().collect(Collectors.toMap(
            Named::key,
            (named) -> named
        )));
        tagEntries.forEach((tag, entries) -> Reflection.HOLDER_SET.invokeBind(
            map2.computeIfAbsent(tag, key -> Reflection.MAPPED_REGISTRY.invokeCreateTag(registry, key)), entries));
        map.forEach(Reflection.HOLDER_REFERENCE::invokeBindTags);
        Reflection.MAPPED_REGISTRY.setAllTags(registry, Reflection.MAPPED_REGISTRY_TAG_SET.invokeFromMap(map2));
    }

    private static void resetTags(MappedRegistry<?> registry) {
        registry.getTags().forEach(entryList -> Reflection.HOLDER_SET.invokeBind(entryList, List.of()));
        Reflection.MAPPED_REGISTRY.getByKey(registry).values().forEach(
            entry -> Reflection.HOLDER_REFERENCE.invokeBindTags(entry, Set.of()));
    }

    // used by BukkitWorldHandle
    public static BlockData createBlockState(@NotNull String data) throws CommandSyntaxException {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        HolderLookup.Provider lookup = server.registryAccess();
        final HolderLookup<Block> blocks = lookup.lookupOrThrow(Registries.BLOCK);
        BlockResult result = BlockStateParser.parseForBlock(blocks, new StringReader(data), true);
        return CraftBlockData.createData(result.blockState());
    }

    public static com.dfsek.terra.api.entity.EntityType getEntityType(@NotNull String data) {
        StringReader reader = new StringReader(data);
        Identifier identifier;
        CompoundTag entityNbt = null;
        try {
            identifier = Identifier.read(reader);
            if(reader.canRead()) {
                if(reader.peek() != '{') throw new IllegalArgumentException("Unexpected data after entity identifier: " + data);
                entityNbt = TagParser.parseCompoundAsArgument(reader);
                if(reader.canRead()) throw new IllegalArgumentException("Unexpected data after entity NBT: " + data);
                entityNbt.putString("id", identifier.toString());
            }
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException("Invalid entity data: " + data, e);
        }

        if(BuiltInRegistries.ENTITY_TYPE.getOptional(identifier).isEmpty()) {
            throw new IllegalArgumentException("Unknown entity type: " + identifier);
        }

        NamespacedKey key = NamespacedKey.fromString(identifier.toString());
        org.bukkit.entity.EntityType bukkitType = key == null ? null : Registry.ENTITY_TYPE.get(key);
        if(bukkitType == null) throw new IllegalArgumentException("Bukkit has no entity type for " + identifier);

        if(entityNbt == null) return new BukkitEntityType(bukkitType);
        return new BukkitEntityTypeExtended(bukkitType, entityNbt.toString());
    }

    public static org.bukkit.entity.Entity spawnEntity(Object target, org.bukkit.Location location,
                                                        com.dfsek.terra.api.entity.EntityType type) {
        if(!(type instanceof BukkitEntityTypeExtended extended)) {
            throw new IllegalArgumentException("Expected a Bukkit entity type with NBT data");
        }
        CompoundTag nbt = parseEntityNbt(extended);

        CraftLimitedRegion limitedRegion = target instanceof CraftLimitedRegion region ? region : null;
        World world = limitedRegion == null ? (World) target : limitedRegion.getWorld();
        if(!(world instanceof CraftWorld craftWorld)) throw new IllegalArgumentException("Unsupported Bukkit world: " + world);
        ServerLevel level = craftWorld.getHandle();

        String entityIDText = nbt.getString("id")
            .orElseThrow(() -> new IllegalArgumentException("Missing entity identifier in NBT"));
        Identifier entityID;
        try {
            entityID = Identifier.read(new StringReader(entityIDText));
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException("Invalid entity identifier in NBT: " + entityIDText, e);
        }
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getOptional(entityID)
            .orElseThrow(() -> new IllegalArgumentException("Unknown entity type in NBT: " + entityID));

        net.minecraft.world.entity.Entity entity = EntityType.loadEntityRecursive(entityType, nbt, level, EntitySpawnReason.STRUCTURE, loaded -> {
            loaded.snapTo(location.getX(), location.getY(), location.getZ(), loaded.getYRot(), loaded.getXRot());
            return loaded;
        });
        if(entity == null) throw new IllegalArgumentException("Could not load entity from NBT: " + nbt);

        if(limitedRegion != null) {
            limitedRegion.addEntityWithPassengers(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } else if(!level.tryAddFreshEntityWithPassengers(entity, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            throw new IllegalStateException("Could not add entity to world: " + nbt);
        }

        return entity.getBukkitEntity();
    }

    public static void setSpawnerEntity(CreatureSpawner spawner, EntityTypeExtended type) {
        if(!(type instanceof BukkitEntityTypeExtended extended)) {
            throw new IllegalArgumentException("Expected a Bukkit entity type with NBT data");
        }
        spawner.setSpawnedEntity(CraftEntitySnapshot.create(parseEntityNbt(extended), extended.getHandle()));
    }

    private static CompoundTag parseEntityNbt(BukkitEntityTypeExtended extended) {
        Object rawData = extended.getData().getHandle();
        if(!(rawData instanceof String snbt)) throw new IllegalArgumentException("Bukkit entity data must contain SNBT text");

        try {
            return TagParser.parseCompoundFully(snbt);
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException("Invalid entity NBT: " + snbt, e);
        }
    }
}
