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

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.Objects;

import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.mod.util.MinecraftUtil;


public class ProtoPlatformBiome implements PlatformBiome {
    private final Identifier identifier;

    private RegistryEntry<Biome> delegate;

    public ProtoPlatformBiome(Identifier identifier) {
        this.identifier = identifier;
    }

    public RegistryKey<Biome> get(Registry<net.minecraft.world.biome.Biome> registry) {
        return MinecraftUtil.getEntry(registry, identifier).orElseThrow().getKey().orElseThrow();
    }

    @Override
    public Identifier getHandle() {
        return identifier;
    }

    public RegistryEntry<Biome> getDelegate() {
        return delegate;
    }

    public void setDelegate(RegistryEntry<Biome> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }
}
