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

package com.dfsek.terra.bukkit.generator;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitPopulatorWrapper extends BlockPopulator {
    private final ChunkGenerator delegate;
    
    public BukkitPopulatorWrapper(ChunkGenerator delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
        delegate.getGenerationStages().forEach(populator -> {
            if(populator instanceof Chunkified) {
                populator.populate(BukkitAdapter.adapt(world), BukkitAdapter.adapt(source));
            }
        });
    }
}
