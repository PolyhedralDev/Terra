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

package com.dfsek.terra.bukkit.handles;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.bukkit.util.BukkitUtils;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;


public class BukkitWorldHandle implements WorldHandle {
    private static final Logger logger = LoggerFactory.getLogger(BukkitWorldHandle.class);
    private final BlockState air;

    public BukkitWorldHandle() {
        this.air = BukkitBlockState.newInstance(Material.AIR.createBlockData());
    }

    @Override
    public synchronized @NotNull BlockState createBlockState(@NotNull String data) {
<<<<<<< HEAD
        org.bukkit.block.data.BlockData bukkitData = Bukkit.createBlockData(
            data); // somehow bukkit managed to make this not thread safe! :)
=======
        // Bukkit's BlockData parser does not support tile-entity NBT (e.g. minecraft:chest{LootTable:'...'})
        // Some packs use that syntax; strip the NBT portion so packs can load, and let loot-fix systems handle loot tables.
        String sanitized = data;
        int nbtStart = data.indexOf('{');
        if(nbtStart >= 0) {
            sanitized = data.substring(0, nbtStart);
        }
        sanitized = sanitized.trim();
        if(sanitized.isEmpty()) {
            sanitized = "minecraft:air";
        }
        org.bukkit.block.data.BlockData bukkitData = Bukkit.createBlockData(sanitized);
>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))
        return BukkitBlockState.newInstance(bukkitData);
    }

    @Override
    public @NotNull BlockState air() {
        return air;
    }

    @Override
    public @NotNull EntityType getEntity(@NotNull String id) {
        return BukkitUtils.getEntityType(id);
    }
}
