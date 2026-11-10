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
import dev.wyck.keys.ResourceKey;
import dev.wyck.level.dimension.Dimension;
import dev.wyck.registry.BiomeRegistry;
import dev.wyck.registry.DimensionRegistry;
import dev.wyck.registry.internal.RegistryId;
import dev.wyck.registry.internal.WyckRegistry;
import dev.wyck.registry.level.LevelStemEditor;
import dev.wyck.tags.TagSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;
import org.bukkit.World;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


public final class WyckBridge {
    private WyckBridge() { }

    /**
     * Wyck's decoder refuses a biome without a registered key.
     */
    public static dev.wyck.biome.Biome biome(Identifier key, Biome biome) {
        MappedRegistry<Biome> owner = new MappedRegistry<>(Registries.BIOME, Lifecycle.stable());
        var holder = owner.register(net.minecraft.resources.ResourceKey.create(Registries.BIOME, key), biome, RegistrationInfo.BUILT_IN);
        owner.freeze();
        return dev.wyck.biome.Biome.decode(holder).toBuilder().resourceKey(key(key)).build();
    }

    private static Dimension dimension(Identifier key, DimensionType type) {
        MappedRegistry<DimensionType> owner = new MappedRegistry<>(Registries.DIMENSION_TYPE, Lifecycle.stable());
        var holder = owner.register(net.minecraft.resources.ResourceKey.create(Registries.DIMENSION_TYPE, key), type,
            RegistrationInfo.BUILT_IN);
        owner.freeze();
        return Dimension.decode(holder).toBuilder().resourceKey(key(key)).build();
    }

    @SuppressWarnings("unchecked")
    public static void registerTimelines(Consumer<WritableRegistry<Timeline>> registration) {
        WyckRegistry.of(RegistryId.TIMELINE).whileUnfrozen(r -> registration.accept((WritableRegistry<Timeline>) r.registry()));
    }

    public static void registerDimension(Identifier key, DimensionType type) {
        DimensionRegistry.registry().register(dimension(key, type));
    }

    public static void applyDimension(World world, Identifier key) {
        LevelStemEditor.create().dimension(Dimension.reference(key(key))).apply(world);
    }

    public static void installGenerator(World world, net.minecraft.world.level.chunk.ChunkGenerator generator) {
        LevelStemEditor.create().chunkGenerator(new MinecraftGenerator(generator)).apply(world);
    }

    public static void registerBiomes(List<dev.wyck.biome.Biome> biomes) {
        BiomeRegistry.registry().register(biomes);
    }

    public static void appendBiomeTag(TagKey<Biome> tag, Collection<Identifier> biomes) {
        Set<dev.wyck.biome.Biome> members = new LinkedHashSet<>();
        for(Identifier biome : biomes) members.add(dev.wyck.biome.Biome.reference(key(biome)));
        TagSet.ofBiomes(key(tag.location()), members).append();
    }

    private static ResourceKey key(Identifier id) {
        return ResourceKey.of(id.getNamespace(), id.getPath());
    }

    /**
     * Wyck installs whatever generator {@link #toMinecraft()} returns.
     */
    private record MinecraftGenerator(net.minecraft.world.level.chunk.ChunkGenerator generator)
        implements dev.wyck.worldgen.chunk.ChunkGenerator {
        @Override
        public dev.wyck.worldgen.biome.BiomeSource biomeSource() {
            return new MinecraftBiomeSource(generator.getBiomeSource());
        }

        @Override
        public Object toMinecraft() {
            return generator;
        }
    }

    private record MinecraftBiomeSource(net.minecraft.world.level.biome.BiomeSource source) implements dev.wyck.worldgen.biome.BiomeSource {
        @Override
        public Object toMinecraft() {
            return source;
        }
    }
}
