package com.dfsek.terra.bukkit.world;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.tag.PostFlattenTagRegistrar;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.BukkitPlatform;
import com.dfsek.terra.bukkit.attribute.PaperVanilla;
import com.dfsek.terra.bukkit.attribute.WyckBridge;
import com.dfsek.terra.minecraft.attribute.BiomeTags;
import com.dfsek.terra.minecraft.attribute.BiomeTags.PlatformTagSource;
import com.dfsek.terra.minecraft.attribute.MinecraftAttributeParents;
import com.dfsek.terra.minecraft.attribute.MinecraftBiomeFactory;
import com.dfsek.terra.minecraft.attribute.PackParents;
import com.dfsek.terra.minecraft.attribute.TerraBiomeIndex;
import com.dfsek.terra.minecraft.attribute.VanillaContext;
import com.dfsek.terra.world.attribute.platform.AttributeReport;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;


public final class BiomeHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeHandler.class);
    private static TerraBiomeIndex biomes = new TerraBiomeIndex();

    private BiomeHandler() { }

    /**
     * Must run on the server thread.
     */
    public static synchronized void registerBiomes(BukkitPlatform platform) {
        MinecraftAttributeParents parents = platform.attributeParents();
        VanillaContext context = parents.requireContext();
        MinecraftBiomeFactory factory = new MinecraftBiomeFactory(platform.getAttributeHandle(), context);
        Registry<Biome> registry = PaperVanilla.server().registryAccess().lookupOrThrow(Registries.BIOME);
        org.bukkit.Registry<org.bukkit.block.Biome> bukkitRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
        boolean playersOnline = !Bukkit.getOnlinePlayers().isEmpty();
        AtomicInteger deferred = new AtomicInteger();

        Run<dev.wyck.biome.Biome> run = collect(platform.getRawConfigRegistry().entries(),
            pack -> parents.prepare(pack, platform.getAttributeHandle().pack(pack), AttributeReport.logging(pack)),
            (pack, packParents, packRun) -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((id, biome) -> {
                BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();
                ResourceKey<Biome> parentKey = ResourceKey.create(Registries.BIOME,
                    Identifier.parse(platformBiome.getHandle().getKey().toString()));
                Biome parent = registry.getValue(parentKey);
                if(parent == null) {
                    LOGGER.error("Pack \"{}\", config \"{}\": biome skipped: no vanilla biome \"{}\"", pack.getID(), biome.getID(),
                        parentKey.identifier());
                    return;
                }
                platformBiome.setParentEntries(parentEntries(() -> parents.biomeEntries(packParents, parentKey, parent), pack.getID(),
                    biome.getID(), LOGGER::warn));

                Identifier terraId = MinecraftBiomeFactory.terraId(pack, id);
                packRun.add(terraId, parentKey.identifier(), registry.containsKey(terraId), () -> {
                    if(playersOnline) {
                        deferred.incrementAndGet();
                        return null;
                    }
                    ResourceKey<VillagerType> parentVillagerType = Objects.requireNonNullElse(context.villagerTypes().apply(parentKey),
                        VillagerType.PLAINS);
                    return registration(() -> build(factory, parentKey, parent, biome, packParents), b -> WyckBridge.biome(terraId, b),
                        parent, parentVillagerType, pack.getID(), biome.getID(), LOGGER::warn);
                }, new PlatformTagSource(packParents.report(), biome.getID(), biome.getPlatformTags()), () -> {
                    NamespacedKey bukkitKey = new NamespacedKey(terraId.getNamespace(), terraId.getPath());
                    org.bukkit.block.Biome bukkitBiome = bukkitRegistry.get(bukkitKey);
                    if(bukkitBiome == null) {
                        LOGGER.error("Pack \"{}\", config \"{}\": biome not registered as \"{}\"", pack.getID(), biome.getID(),
                            terraId);
                        return;
                    }
                    platformBiome.getContext().put(new BukkitBiomeInfo(dev.wyck.keys.ResourceKey.of(bukkitKey), bukkitBiome));
                }, pack.getID(), biome.getID(), LOGGER::warn);
            }), LOGGER::warn);

        if(deferred.get() > 0) {
            LOGGER.info("{} new custom biomes generate as their vanilla parents until the server restarts (players were online during the reload).",
                        deferred.get());
        }
        registerNew(run.toRegister, WyckBridge::registerBiomes, LOGGER::info);
        Map<ResourceKey<Biome>, ResourceKey<VillagerType>> villagerTypes = PaperVanilla.villagerTypes();
        run.villagerTypes.forEach((terraId, type) -> villagerTypes.put(ResourceKey.create(Registries.BIOME, terraId), type));
        biomes = run.biomes;
        run.afterRegistration.forEach(Runnable::run);
    }

    static <P, B> Run<B> collect(Iterable<ConfigPack> packs, Function<ConfigPack, P> prepare, PackCollector<P, B> collectPack,
                                 Consumer<String> warn) {
        Run<B> run = new Run<>(null);
        for(ConfigPack pack : packs) {
            P prepared;
            try {
                prepared = prepare.apply(pack);
            } catch(RuntimeException e) {
                LOGGER.debug("Failed to read the attributes of pack {}", pack.getID(), e);
                warn.accept("Pack \"" + pack.getID() + "\": biomes skipped: its attributes could not be read");
                continue;
            }
            Run<B> packRun = new Run<>(run);
            try {
                collectPack.collect(pack, prepared, packRun);
            } catch(RuntimeException e) {
                LOGGER.debug("Failed to collect the biomes of pack {}", pack.getID(), e);
                warn.accept("Pack \"" + pack.getID() + "\": biomes skipped: they could not be built");
                continue;
            }
            run.addAll(packRun);
        }
        return run;
    }

    @FunctionalInterface
    interface PackCollector<P, B> {
        void collect(ConfigPack pack, P prepared, Run<B> run);
    }

    static AttributeSlots parentEntries(Supplier<AttributeSlots> read, String pack, String config, Consumer<String> warn) {
        try {
            return read.get();
        } catch(RuntimeException e) {
            LOGGER.debug("Failed to read the parent attributes of Terra biome {} of pack {}", config, pack, e);
            warn.accept("Pack \"" + pack + "\", config \"" + config + "\": parent attributes ignored: they could not be read");
            return AttributeSlots.EMPTY;
        }
    }

    static final class Run<B> {
        private final @Nullable Run<B> before;
        final List<B> toRegister = new ArrayList<>();
        final Map<Identifier, ResourceKey<VillagerType>> villagerTypes = new LinkedHashMap<>();
        final TerraBiomeIndex biomes = new TerraBiomeIndex();
        final List<Runnable> afterRegistration = new ArrayList<>();

        Run(@Nullable Run<B> before) {
            this.before = before;
        }

        void add(Identifier terraId, Identifier parent, boolean registered, Supplier<@Nullable Registration<B>> registration,
                 PlatformTagSource tags, Runnable after, String pack, String config, Consumer<String> warn) {
            if(recorded(terraId)) {
                warn.accept(TerraBiomeIndex.sharedBiome(pack, config, terraId));
                afterRegistration.add(after);
                return;
            }
            if(!registered) {
                Registration<B> queued = registration.get();
                if(queued == null) return;
                toRegister.add(queued.biome());
                villagerTypes.put(terraId, queued.villagerType());
            }
            biomes.record(terraId, parent, tags);
            afterRegistration.add(after);
        }

        private boolean recorded(Identifier terraId) {
            return biomes.contains(terraId) || (before != null && before.recorded(terraId));
        }

        private void addAll(Run<B> run) {
            toRegister.addAll(run.toRegister);
            villagerTypes.putAll(run.villagerTypes);
            biomes.addAll(run.biomes);
            afterRegistration.addAll(run.afterRegistration);
        }
    }

    static <B> void registerNew(List<B> biomes, Consumer<List<B>> register, Consumer<String> info) {
        if(biomes.isEmpty()) {
            info.accept("No new custom biomes to register.");
            return;
        }
        info.accept("Registering " + biomes.size() + " custom biomes through Wyck...");
        register.accept(biomes);
    }

    public static MinecraftBiomeFactory.Built build(MinecraftBiomeFactory factory, ResourceKey<Biome> parentKey, Biome parent,
                                                    com.dfsek.terra.api.world.biome.Biome terraBiome, PackParents pack) {
        return factory.create(parentKey, parent, terraBiome, pack, pack.report(), BiomeGenerationSettings.EMPTY);
    }

    /**
     * Must run on the server thread.
     */
    public static synchronized void addToVanillaTags(BukkitPlatform platform) {
        Registry<Biome> registry = PaperVanilla.server().registryAccess().lookupOrThrow(Registries.BIOME);
        Map<TagKey<Biome>, List<Holder<Biome>>> before = new LinkedHashMap<>();
        registry.getTags().forEach(tag -> before.put(tag.key(), new ArrayList<>(tag.stream().toList())));
        Map<TagKey<Biome>, List<Holder<Biome>>> after = new LinkedHashMap<>();
        before.forEach((tag, members) -> after.put(tag, new ArrayList<>(members)));

        BiomeTags.addTerraBiomes(after, id -> registry.get(id).map(h -> h), biomes);

        Map<TagKey<Biome>, List<Identifier>> additions = additions(before, after);
        int added = appendAll(additions, WyckBridge::appendBiomeTag, "wyck could not append to the tag", LOGGER::warn);
        LOGGER.info("Added Terra biomes to {} biome tags.", added);
    }

    /**
     * Must not throw: an exception fails the data pack reload.
     */
    public static synchronized void addToReloadedTags(PostFlattenTagRegistrar<org.bukkit.block.Biome> registrar) {
        try {
            Registry<Biome> registry = PaperVanilla.server().registryAccess().lookupOrThrow(Registries.BIOME);
            Map<TagKey<Biome>, List<Identifier>> tags = new LinkedHashMap<>();
            registrar.getAllTags().forEach((tag, members) -> tags.put(TagKey.create(Registries.BIOME, identifier(tag.key())),
                members.stream().map(member -> identifier(member.key())).toList()));

            Map<TagKey<Biome>, List<Identifier>> additions = reloadAdditions(tags, id -> registry.get(id).map(h -> h), biomes);
            int added = appendAll(additions, (tag, biomes) -> registrar.addToTag(
                io.papermc.paper.registry.tag.TagKey.create(RegistryKey.BIOME, key(tag.location())),
                biomes.stream().map(biome -> TypedKey.create(RegistryKey.BIOME, key(biome))).toList()),
                "the reload could not add them", LOGGER::warn);
            LOGGER.info("Added Terra biomes to {} reloaded biome tags.", added);
        } catch(RuntimeException e) {
            LOGGER.debug("Failed to add Terra biomes to the reloaded biome tags", e);
            LOGGER.warn("Terra biomes not added to the reloaded biome tags; restart the server to restore them");
        }
    }

    /**
     * Parent tags come from {@code tags}: the parent's holder still has its pre-reload tags.
     */
    static Map<TagKey<Biome>, List<Identifier>> reloadAdditions(Map<TagKey<Biome>, List<Identifier>> tags,
                                                                Function<Identifier, Optional<Holder<Biome>>> lookup,
                                                                TerraBiomeIndex biomes) {
        Map<TagKey<Biome>, List<Holder<Biome>>> before = new LinkedHashMap<>();
        Map<TagKey<Biome>, List<Holder<Biome>>> after = new LinkedHashMap<>();
        tags.forEach((tag, members) -> {
            List<Holder<Biome>> old = members.stream().map(lookup).flatMap(Optional::stream).toList();
            List<Holder<Biome>> withTerra = new ArrayList<>(old);
            for(Identifier member : members) {
                for(Identifier terra : biomes.children(member)) {
                    lookup.apply(terra).ifPresentOrElse(withTerra::add,
                        () -> LOGGER.debug("Terra biome {} is not registered; left out of reloaded tag {}", terra,
                            tag.location()));
                }
            }
            before.put(tag, old);
            after.put(tag, withTerra);
        });
        BiomeTags.addPlatformTags(after, lookup, biomes.tags());
        return additions(before, after);
    }

    private static Identifier identifier(Key key) {
        return Identifier.fromNamespaceAndPath(key.namespace(), key.value());
    }

    private static Key key(Identifier id) {
        return Key.key(id.getNamespace(), id.getPath());
    }

    public static Map<TagKey<Biome>, List<Identifier>> additions(Map<TagKey<Biome>, List<Holder<Biome>>> before,
                                                                Map<TagKey<Biome>, List<Holder<Biome>>> after) {
        Map<TagKey<Biome>, List<Identifier>> additions = new LinkedHashMap<>();
        after.forEach((tag, members) -> {
            Set<Holder<Biome>> old = new HashSet<>(before.getOrDefault(tag, List.of()));
            Set<Identifier> added = new LinkedHashSet<>();
            for(Holder<Biome> member : members) {
                if(!old.contains(member)) member.unwrapKey().ifPresent(key -> added.add(key.identifier()));
            }
            if(!added.isEmpty()) additions.put(tag, List.copyOf(added));
        });
        return additions;
    }

    record Registration<B>(B biome, ResourceKey<VillagerType> villagerType) { }

    static <B> @Nullable Registration<B> registration(Supplier<MinecraftBiomeFactory.Built> build, Function<Biome, B> wyck,
                                                      Biome parent, ResourceKey<VillagerType> parentVillagerType, String pack,
                                                      String config, Consumer<String> warn) {
        String prefix = "Pack \"" + pack + "\", config \"" + config + "\": ";
        MinecraftBiomeFactory.Built built;
        try {
            built = build.get();
        } catch(RuntimeException e) {
            LOGGER.debug("Failed to build Terra biome {} of pack {}", config, pack, e);
            return parent(wyck, parent, parentVillagerType, prefix, "its attributes could not be applied",
                "its attributes could not be applied, and wyck could not read its parent", warn);
        }
        try {
            return new Registration<>(wyck.apply(built.biome()), built.villagerType());
        } catch(RuntimeException e) {
            LOGGER.debug("Wyck failed to read Terra biome {} of pack {}", config, pack, e);
            return parent(wyck, parent, parentVillagerType, prefix, "wyck could not read its attributes",
                "wyck could not read its attributes or its parent", warn);
        }
    }

    private static <B> @Nullable Registration<B> parent(Function<Biome, B> wyck, Biome parent, ResourceKey<VillagerType> villagerType,
                                                        String prefix, String reason, String skipReason, Consumer<String> warn) {
        try {
            Registration<B> registration = new Registration<>(wyck.apply(parent), villagerType);
            warn.accept(prefix + "biome built from its parent: " + reason);
            return registration;
        } catch(RuntimeException e) {
            LOGGER.debug("Wyck failed to read the parent of a Terra biome", e);
            warn.accept(prefix + "biome skipped: " + skipReason);
            return null;
        }
    }

    static int appendAll(Map<TagKey<Biome>, List<Identifier>> additions, BiConsumer<TagKey<Biome>, List<Identifier>> append,
                         String failure, Consumer<String> warn) {
        int appended = 0;
        for(Map.Entry<TagKey<Biome>, List<Identifier>> entry : additions.entrySet()) {
            try {
                append.accept(entry.getKey(), entry.getValue());
                appended++;
            } catch(RuntimeException e) {
                LOGGER.debug("Failed to append Terra biomes to biome tag {}", entry.getKey().location(), e);
                warn.accept("Biome tag \"" + entry.getKey().location() + "\": terra biomes not added: " + failure);
            }
        }
        return appended;
    }

}
