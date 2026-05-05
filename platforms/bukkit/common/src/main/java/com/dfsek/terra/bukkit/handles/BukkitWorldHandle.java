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
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.bukkit.util.BukkitUtils;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BukkitWorldHandle implements WorldHandle {
    private static final Logger logger = LoggerFactory.getLogger(BukkitWorldHandle.class);
    private final BlockState air;

    public BukkitWorldHandle() {
        this.air = BukkitBlockState.newInstance(Material.AIR.createBlockData());
    }

    @Override
    public synchronized @NotNull BlockState createBlockState(@NotNull String data) {
        try {
            org.bukkit.block.data.BlockData bukkitData = Bukkit.createBlockData(data); // somehow bukkit managed to make this not thread safe! :)
            return BukkitBlockState.newInstance(bukkitData);
        } catch (Exception ignored) {
            try {
                Class<?> hacks = Class.forName("com.dfsek.terra.bukkit.nms.AwfulBukkitHacks");
                Method method = hacks.getMethod("createBlockState", String.class);
                Object result = method.invoke(null, data);
                return BukkitBlockState.newInstance((BlockData) result);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Invalid block state data: " + data);
            }
        }
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
