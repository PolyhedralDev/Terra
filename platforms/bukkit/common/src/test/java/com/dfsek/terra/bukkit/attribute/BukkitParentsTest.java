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

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.junit.jupiter.api.Test;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributes;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.minecraft.attribute.FakePackAttributes;
import com.dfsek.terra.minecraft.attribute.MinecraftAttributeParents;
import com.dfsek.terra.minecraft.attribute.PackParents;
import com.dfsek.terra.minecraft.attribute.TestBiome;
import com.dfsek.terra.world.attribute.platform.AttributeReport;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;

import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.CONTEXT;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.HANDLE;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.REGISTRIES;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.attribute;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.registry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BukkitParentsTest {
    private static final Registry<Attribute<?>> REGISTRY = registry();
    private static final ConfigPack PACK = pack("test");
    private static final Attribute<Boolean> PRECIPITATION = attribute(MinecraftAttributes.BIOME_HAS_PRECIPITATION.getRegistryKey().toString());
    private static final Attribute<Boolean> HAS_CEILING = attribute(MinecraftAttributes.DIMENSION_HAS_CEILING.getRegistryKey().toString());

    private final List<String> warnings = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, s -> { });
    private final MinecraftAttributeParents parents = new MinecraftAttributeParents(HANDLE);

    private static ConfigPack pack(String id) {
        return (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getID" -> id;
                case "getRegistry" -> REGISTRY;
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                case "toString" -> "ConfigPack[" + id + "]";
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static Biome desert() {
        return REGISTRIES.lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.DESERT).value();
    }

    @Test
    void vanillaVillagerTypes() {
        Map<ResourceKey<Biome>, ResourceKey<VillagerType>> types = PaperVanilla.villagerTypes();
        assertSame(types, PaperVanilla.villagerTypes(), "read once");
        assertEquals(VillagerType.DESERT, types.get(Biomes.DESERT));

        ResourceKey<Biome> test = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("terra", "test_biome"));
        ResourceKey<VillagerType> before = types.get(test);
        try {
            types.put(test, VillagerType.SNOW);
            assertEquals(VillagerType.SNOW, types.get(test));
            assertEquals(VillagerType.SNOW, PaperVanilla.context(REGISTRIES).villagerTypes().apply(test));
        } finally {
            if(before == null) types.remove(test);
            else types.put(test, before);
        }
        assertEquals(before, types.get(test));
    }

    @Test
    void parentEntriesAllocationFree() {
        parents.beginLoad(CONTEXT);
        FakePackAttributes fake = new FakePackAttributes(HANDLE, parents, AttributeMap.empty(), Map.of(), Map.of());
        PackParents pack = parents.prepare(PACK, Optional.of(fake), report);
        BukkitPlatformBiome platformBiome = new BukkitPlatformBiome(null);
        assertSame(AttributeSlots.EMPTY, platformBiome.parentEntries());
        assertThrows(NullPointerException.class, () -> platformBiome.setParentEntries(null));
        TestBiome biome = new TestBiome("DESERT", 0, platformBiome);
        assertNull(parents.biomeOrNull(biome, PRECIPITATION));
        platformBiome.setParentEntries(parents.biomeEntries(pack, Biomes.DESERT, desert()));
        assertEquals(AttributeEntry.override(false), parents.biomeOrNull(biome, PRECIPITATION));
        TestBiome plain = new TestBiome("PLAIN", 1, (PlatformBiome) () -> "plain");
        assertEquals(List.of(), warnings);
        assertEquals(7, reads(fake, biome, plain), "every read sees the expected entry");

        com.sun.management.ThreadMXBean threads = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();
        long thread = Thread.currentThread().threadId();
        int sink = 0;
        for(int i = 0; i < 100_000; i++) sink += reads(fake, biome, plain);
        long before = threads.getThreadAllocatedBytes(thread);
        for(int i = 0; i < 10_000; i++) sink += reads(fake, biome, plain);
        long allocated = threads.getThreadAllocatedBytes(thread) - before;

        assertEquals(110_000 * 7, sink);
        assertTrue(allocated < 1024, allocated + " bytes allocated");
    }

    private int reads(FakePackAttributes fake, TestBiome biome, TestBiome plain) {
        int hash = 0;
        hash += parents.biomeOrNull(biome, PRECIPITATION) == null ? 0 : 1;
        hash += parents.biomeOrNull(plain, PRECIPITATION) == null ? 2 : 0;
        hash += parents.packOrNull(fake, HAS_CEILING) == null ? 0 : 4;
        return hash;
    }
}
