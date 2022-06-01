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

package com.dfsek.terra.bukkit;

import org.bukkit.Location;

import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitEntity implements Entity {
    private final org.bukkit.entity.Entity entity;
    
    public BukkitEntity(org.bukkit.entity.Entity entity) {
        this.entity = entity;
    }
    
    @Override
    public org.bukkit.entity.Entity getHandle() {
        return entity;
    }
    
    @Override
    public Vector3 position() {
        return BukkitAdapter.adapt(entity.getLocation().toVector());
    }
    
    @Override
    public void position(Vector3 location) {
        entity.teleport(BukkitAdapter.adapt(location).toLocation(entity.getWorld()));
    }
    
    @Override
    public void world(ServerWorld world) {
        Location newLoc = entity.getLocation().clone();
        newLoc.setWorld(BukkitAdapter.adapt(world));
        entity.teleport(newLoc);
    }
    
    @Override
    public ServerWorld world() {
        return BukkitAdapter.adapt(entity.getWorld());
    }
}
