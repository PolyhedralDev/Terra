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

package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.PropertyHolder;
import com.dfsek.terra.api.world.biome.PlatformBiome;


public class BukkitPlatformBiome implements PlatformBiome, PropertyHolder {
    private final org.bukkit.block.Biome biome;
    private final Context context = new Context();
    
    public BukkitPlatformBiome(org.bukkit.block.Biome biome) {
        this.biome = biome;
    }
    
    @Override
    public org.bukkit.block.Biome getHandle() {
        return biome;
    }
    
    @Override
    public Context getContext() {
        return context;
    }
}
