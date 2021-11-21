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

import com.dfsek.tectonic.exception.LoadException;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Locale;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;


public class BukkitWorldHandle implements WorldHandle {
    private final BlockState air;
    
    public BukkitWorldHandle() {
        this.air = BukkitBlockState.newInstance(Material.AIR.createBlockData());
    }
    
    @Override
    public BlockState createBlockData(String data) {
        org.bukkit.block.data.BlockData bukkitData = Bukkit.createBlockData(data);
        return BukkitBlockState.newInstance(bukkitData);
    }
    
    @Override
    public BlockState air() {
        return air;
    }
    
    @Override
    public BlockEntity createBlockEntity(Vector3 location, BlockState block, String snbt) {
        return null;
    }
    
    @Override
    public EntityType getEntity(String id) {
        if(!id.startsWith("minecraft:")) throw new LoadException("Invalid entity identifier " + id);
        return new BukkitEntityType(org.bukkit.entity.EntityType.valueOf(id.toUpperCase(Locale.ROOT).substring(10)));
    }

}
