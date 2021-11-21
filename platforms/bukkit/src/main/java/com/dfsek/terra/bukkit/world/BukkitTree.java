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

import org.bukkit.TreeType;

import java.util.Locale;
import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.World;


public class BukkitTree implements Tree {
    private final TreeType delegate;
    private final MaterialSet spawnable;
    private final Platform platform;
    
    public BukkitTree(TreeType delegate, Platform platform) {
        this.delegate = delegate;
        this.platform = platform;
        this.spawnable = getSpawnable(delegate);
    }
    
    private MaterialSet getSpawnable(TreeType type) {
        WorldHandle handle = platform.getWorldHandle();
        return switch(type) {
            case CRIMSON_FUNGUS -> MaterialSet.get(handle.createBlockData("minecraft:crimson_nylium"));
            case WARPED_FUNGUS -> MaterialSet.get(handle.createBlockData("minecraft:warped_nylium"));
            case BROWN_MUSHROOM, RED_MUSHROOM -> MaterialSet.get(handle.createBlockData("minecraft:mycelium"),
                                                                 handle.createBlockData("minecraft:grass_block"),
                                                                 handle.createBlockData("minecraft:podzol"));
            case CHORUS_PLANT -> MaterialSet.get(handle.createBlockData("minecraft:end_stone"));
            default -> MaterialSet.get(handle.createBlockData("minecraft:grass_block"), handle.createBlockData("minecraft:dirt"),
                                       handle.createBlockData("minecraft:podzol"));
        };
    }
    
    @Override
    @SuppressWarnings("try")
    public boolean plant(Vector3 l, World world, Random r) {
        try(ProfileFrame ignore = platform.getProfiler().profile("bukkit_tree:" + delegate.toString().toLowerCase(Locale.ROOT))) {
            return BukkitAdapter.adapt(world).generateTree(BukkitAdapter.adapt(l).toLocation(BukkitAdapter.adapt(world)), delegate);
        }
    }
    
    @Override
    public MaterialSet getSpawnable() {
        return spawnable;
    }
}
