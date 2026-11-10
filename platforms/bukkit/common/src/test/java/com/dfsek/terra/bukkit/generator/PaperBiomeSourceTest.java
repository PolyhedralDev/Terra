package com.dfsek.terra.bukkit.generator;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;
import com.dfsek.terra.minecraft.attribute.MinecraftTestSupport;
import com.dfsek.terra.minecraft.attribute.TestBiome;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PaperBiomeSourceTest {
    private static final HolderGetter<Biome> BIOMES = MinecraftTestSupport.REGISTRIES.lookupOrThrow(Registries.BIOME);
    private static final ResourceKey<Biome> UNREGISTERED = ResourceKey.create(Registries.BIOME, Identifier.parse("terra:late"));
    private static final TestBiome DESERT = new TestBiome("desert", 0, Biomes.DESERT.identifier());
    private static final TestBiome PLAINS = new TestBiome("plains", 1, Biomes.PLAINS.identifier());
    private static final Map<com.dfsek.terra.api.world.biome.Biome, ResourceKey<Biome>> KEYS = Map.of(DESERT, Biomes.DESERT, PLAINS,
        Biomes.PLAINS);

    private final List<List<Integer>> queries = new ArrayList<>();

    @Test
    void resolvesAtBlockCoordinatesWithoutTheSampler() {
        PaperBiomeSource source = source(pack(provider(DESERT)));

        Holder<Biome> holder = source.createResolver(null).getNoiseBiome(3, -2, 5);

        assertEquals(Biomes.DESERT, key(holder));
        assertEquals(List.of(List.of(12, -8, 20)), queries);
    }

    @Test
    void possibleBiomesAreThePacksBiomes() {
        PaperBiomeSource source = source(pack(provider(DESERT, DESERT, PLAINS)));

        assertEquals(Set.of(Biomes.DESERT, Biomes.PLAINS),
            source.possibleBiomes().stream().map(PaperBiomeSourceTest::key).collect(Collectors.toSet()));
    }

    @Test
    void followsAReloadedPack() {
        BukkitChunkGeneratorWrapper wrapper = new BukkitChunkGeneratorWrapper(null, pack(provider(DESERT)), null);
        PaperBiomeSource source = new PaperBiomeSource(wrapper, biome -> BIOMES.getOrThrow(KEYS.get(biome)), 0);

        wrapper.setPack(pack(provider(PLAINS)));

        assertEquals(Biomes.PLAINS, key(source.biome(0, 0, 0)));
    }

    @Test
    void registeredBiomeWins() {
        assertEquals(Biomes.DESERT, key(PaperBiomeSource.holder(BIOMES, Biomes.DESERT, Biomes.JUNGLE, "desert")));
    }

    @Test
    void unregisteredBiomeFallsBackToItsParent() {
        assertEquals(Biomes.JUNGLE, key(PaperBiomeSource.holder(BIOMES, UNREGISTERED, Biomes.JUNGLE, "late")));
    }

    @Test
    void neitherRegisteredFails() {
        assertThrows(IllegalStateException.class, () -> PaperBiomeSource.holder(BIOMES, UNREGISTERED, UNREGISTERED, "late"));
    }

    private PaperBiomeSource source(ConfigPack pack) {
        return new PaperBiomeSource(new BukkitChunkGeneratorWrapper(null, pack, null), biome -> BIOMES.getOrThrow(KEYS.get(biome)), 0);
    }

    private BiomeProvider provider(com.dfsek.terra.api.world.biome.Biome first, com.dfsek.terra.api.world.biome.Biome... rest) {
        List<com.dfsek.terra.api.world.biome.Biome> all = new ArrayList<>(List.of(rest));
        if(!all.contains(first)) all.addFirst(first);
        return new BiomeProvider() {
            @Override
            public com.dfsek.terra.api.world.biome.Biome getBiome(int x, int y, int z, long seed) {
                queries.add(List.of(x, y, z));
                return first;
            }

            @Override
            public Iterable<com.dfsek.terra.api.world.biome.Biome> getBiomes() {
                return all;
            }
        };
    }

    private static ConfigPack pack(BiomeProvider provider) {
        ChunkGeneratorProvider generators = pack -> null;
        return (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getBiomeProvider" -> provider;
                case "getGeneratorProvider" -> generators;
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static ResourceKey<Biome> key(Holder<Biome> holder) {
        return holder.unwrapKey().orElseThrow();
    }
}
