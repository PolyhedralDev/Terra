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

import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.world.BiomeGrid;
import com.dfsek.terra.api.world.biome.Biome;


@SuppressWarnings("deprecation")
public class BukkitBiomeGrid implements BiomeGrid {
    private final ChunkGenerator.BiomeGrid delegate;
    
    public BukkitBiomeGrid(ChunkGenerator.BiomeGrid biomeGrid) {
        this.delegate = biomeGrid;
    }
    
    @Override
    public ChunkGenerator.BiomeGrid getHandle() {
        return delegate;
    }
    
    @Override
    public void setBiome(int x, int z, @NotNull Biome bio) {
        delegate.setBiome(x, z, ((BukkitBiome) bio).getHandle());
    }
    
    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome bio) {
        delegate.setBiome(x, y, z, ((BukkitBiome) bio).getHandle());
    }
    
    @Override
    public @NotNull Biome getBiome(int x, int z) {
        return new BukkitBiome(delegate.getBiome(x, z));
    }
    
    @Override
    public @NotNull Biome getBiome(int x, int y, int z) {
        return new BukkitBiome(delegate.getBiome(x, y, z));
    }
}
