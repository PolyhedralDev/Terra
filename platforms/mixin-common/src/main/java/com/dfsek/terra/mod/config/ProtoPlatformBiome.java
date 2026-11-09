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

package com.dfsek.terra.mod.config;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Objects;

import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.mod.util.MinecraftUtil;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;
import com.dfsek.terra.world.attribute.platform.ParentEntryHolder;


public class ProtoPlatformBiome implements PlatformBiome, ParentEntryHolder {
    private final Identifier identifier;

    private Holder<Biome> delegate;

    // Volatile for generation threads; AttributeSlots is immutable.
    private volatile AttributeSlots parentEntries = AttributeSlots.EMPTY;

    public ProtoPlatformBiome(Identifier identifier) {
        this.identifier = identifier;
    }

    public ResourceKey<Biome> get(Registry<net.minecraft.world.level.biome.Biome> registry) {
        return MinecraftUtil.getEntry(registry, identifier).orElseThrow().unwrapKey().orElseThrow();
    }

    @Override
    public Identifier getHandle() {
        return identifier;
    }

    public Holder<Biome> getDelegate() {
        return delegate;
    }

    public void setDelegate(Holder<Biome> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public AttributeSlots parentEntries() {
        return parentEntries;
    }

    public void setParentEntries(AttributeSlots parentEntries) {
        this.parentEntries = Objects.requireNonNull(parentEntries);
    }
}
