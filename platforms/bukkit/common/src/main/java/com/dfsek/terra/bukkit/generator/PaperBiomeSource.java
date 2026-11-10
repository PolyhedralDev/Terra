package com.dfsek.terra.bukkit.generator;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.bukkit.NamespacedKey;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;


/**
 * Biomes come straight from the pack's biome provider, with no climate sampling.
 */
public final class PaperBiomeSource extends BiomeSource {
    private final BukkitChunkGeneratorWrapper generator;
    private final Function<com.dfsek.terra.api.world.biome.Biome, Holder<Biome>> vanilla;
    private final long seed;
    private volatile Biomes biomes;

    public PaperBiomeSource(BukkitChunkGeneratorWrapper generator, Function<com.dfsek.terra.api.world.biome.Biome, Holder<Biome>> vanilla,
                            long seed) {
        this.generator = generator;
        this.vanilla = vanilla;
        this.seed = seed;
        this.biomes = biomes(generator.getPack());
    }

    public Holder<Biome> biome(int quartX, int quartY, int quartZ) {
        Biomes current = current();
        com.dfsek.terra.api.world.biome.Biome biome = current.provider.getBiome(quartX << 2, quartY << 2, quartZ << 2, seed);
        Holder<Biome> holder = current.holders.get(biome);
        return holder != null ? holder : vanilla.apply(biome);
    }

    @Override
    public BiomeResolver createResolver(Sampler sampler) {
        return this::biome;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return current().holders.values().stream().distinct();
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return MapCodec.assumeMapUnsafe(BiomeSource.CODEC);
    }

    private Biomes current() {
        Biomes current = biomes;
        ConfigPack pack = generator.getPack();
        if(current.pack == pack) return current;
        Biomes reloaded = biomes(pack);
        biomes = reloaded;
        return reloaded;
    }

    private Biomes biomes(ConfigPack pack) {
        BiomeProvider provider = pack.getBiomeProvider();
        Map<com.dfsek.terra.api.world.biome.Biome, Holder<Biome>> holders = new IdentityHashMap<>();
        for(com.dfsek.terra.api.world.biome.Biome biome : provider.getBiomes()) holders.put(biome, vanilla.apply(biome));
        return new Biomes(pack, provider, holders);
    }

    public static Function<com.dfsek.terra.api.world.biome.Biome, Holder<Biome>> registered(HolderGetter<Biome> registry) {
        return biome -> holder(registry, key(BukkitBiomeProvider.toBukkit(biome).getKey()),
            key(((BukkitPlatformBiome) biome.getPlatformBiome()).getHandle().getKey()), biome.getID());
    }

    /**
     * Falls back to the vanilla parent for a biome added by a reload while players were online.
     */
    static Holder<Biome> holder(HolderGetter<Biome> registry, ResourceKey<Biome> registered, ResourceKey<Biome> parent, String id) {
        return registry.get(registered)
            .or(() -> registry.get(parent))
            .orElseThrow(() -> new IllegalStateException("Biome " + id + " has no registered vanilla biome"));
    }

    private static ResourceKey<Biome> key(NamespacedKey key) {
        return ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(key.getNamespace(), key.getKey()));
    }

    private record Biomes(ConfigPack pack, BiomeProvider provider, Map<com.dfsek.terra.api.world.biome.Biome, Holder<Biome>> holders) {
    }
}
