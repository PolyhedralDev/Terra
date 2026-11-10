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

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.world.BiomeHandler;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.minecraft.attribute.MinecraftAttributeParents;
import com.dfsek.terra.minecraft.attribute.MinecraftBiomeFactory;
import com.dfsek.terra.minecraft.attribute.PackParents;
import com.dfsek.terra.minecraft.attribute.TestBiome;
import com.dfsek.terra.minecraft.attribute.VanillaCodecs;
import com.dfsek.terra.minecraft.attribute.VanillaContext;
import com.dfsek.terra.world.attribute.platform.AttributeReport;

import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.HANDLE;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.REGISTRIES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PaperBiomeTest {
    private static final String CONFIG = "BIOME";
    private static final VanillaContext PAPER = PaperVanilla.context(REGISTRIES);

    private final List<String> warnings = new ArrayList<>();
    private final List<String> debugs = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, debugs::add);
    private final MinecraftAttributeParents parents = new MinecraftAttributeParents(HANDLE);
    private final MinecraftBiomeFactory factory = new MinecraftBiomeFactory(HANDLE, PAPER);

    private static ConfigPack pack(String id) {
        return (ConfigPack) Proxy.newProxyInstance(ConfigPack.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "getID" -> id;
                case "getRegistry" -> null;
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                case "toString" -> "ConfigPack[" + id + "]";
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static Holder.Reference<Biome> vanilla(ResourceKey<Biome> key) {
        return REGISTRIES.lookupOrThrow(Registries.BIOME).getOrThrow(key);
    }

    private static Object network(Biome biome) {
        return VanillaCodecs.encode(Biome.NETWORK_CODEC, biome, PAPER.ops());
    }

    private MinecraftBiomeFactory.Built build(TestBiome biome, PackParents pack, ResourceKey<Biome> parent) {
        Biome parentBiome = vanilla(parent).value();
        ((BukkitPlatformBiome) biome.getPlatformBiome()).setParentEntries(parents.biomeEntries(pack, parent, parentBiome));
        return BiomeHandler.build(factory, parent, parentBiome, biome, pack);
    }

    @Test
    void noAttributesMatchesParent() {
        Biome desert = vanilla(Biomes.DESERT).value();
        assertFalse(desert.getGenerationSettings().features().isEmpty(), "the desert has features");
        assertTrue(desert.getGenerationSettings().getCarvers().iterator().hasNext(), "the desert has carvers");
        parents.beginLoad(PAPER);
        PackParents bare = parents.prepare(pack("bare"), Optional.empty(), report);
        TestBiome biome = new TestBiome(CONFIG, 0, new BukkitPlatformBiome(null));

        MinecraftBiomeFactory.Built built = build(biome, bare, Biomes.DESERT);

        assertEquals(network(desert), network(built.biome()));
        assertEquals(List.of(), built.biome().getGenerationSettings().features());
        assertFalse(built.biome().getGenerationSettings().getCarvers().iterator().hasNext(), "no carvers");
        assertEquals(VillagerType.DESERT, built.villagerType());
        assertEquals(List.of(), warnings);
        assertEquals(List.of(), debugs);
    }

    @Test
    void additionsListOnlyNewMembers() {
        MappedRegistry<Biome> biomes = new MappedRegistry<>(Registries.BIOME, Lifecycle.stable());
        Holder<Biome> forest = biomes.register(Biomes.FOREST, vanilla(Biomes.FOREST).value(), RegistrationInfo.BUILT_IN);
        Holder<Biome> ocean = biomes.register(Biomes.OCEAN, vanilla(Biomes.OCEAN).value(), RegistrationInfo.BUILT_IN);
        Identifier terraId = Identifier.fromNamespaceAndPath("terra", "t/b");
        Holder<Biome> terra = biomes.register(ResourceKey.create(Registries.BIOME, terraId), vanilla(Biomes.PLAINS).value(),
            RegistrationInfo.BUILT_IN);
        TagKey<Biome> isForest = TagKey.create(Registries.BIOME, Identifier.withDefaultNamespace("is_forest"));
        TagKey<Biome> isOcean = TagKey.create(Registries.BIOME, Identifier.withDefaultNamespace("is_ocean"));
        TagKey<Biome> hasSnow = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("mypack", "has_snow"));

        Map<TagKey<Biome>, List<Holder<Biome>>> before = new LinkedHashMap<>();
        before.put(isForest, List.of(forest));
        before.put(isOcean, List.of(ocean));
        Map<TagKey<Biome>, List<Holder<Biome>>> after = new LinkedHashMap<>();
        after.put(isForest, List.of(forest, terra));
        after.put(isOcean, List.of(ocean));
        after.put(hasSnow, List.of(terra));

        assertEquals(Map.of(isForest, List.of(terraId), hasSnow, List.of(terraId)), BiomeHandler.additions(before, after));

        before.put(isForest, List.of(forest, terra));
        after.put(isForest, List.of(forest, terra, terra));
        assertEquals(Map.of(hasSnow, List.of(terraId)), BiomeHandler.additions(before, after));
    }
}
