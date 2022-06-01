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

import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitPlayer implements Player {
    private final org.bukkit.entity.Player delegate;
    
    public BukkitPlayer(org.bukkit.entity.Player delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public org.bukkit.entity.Player getHandle() {
        return delegate;
    }
    
    @Override
    public Vector3 position() {
        org.bukkit.Location bukkit = delegate.getLocation();
        return Vector3.of(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
    
    @Override
    public void position(Vector3 location) {
        delegate.teleport(BukkitAdapter.adapt(location).toLocation(delegate.getWorld()));
    }
    
    @Override
    public void world(ServerWorld world) {
        Location newLoc = delegate.getLocation().clone();
        newLoc.setWorld(BukkitAdapter.adapt(world));
        delegate.teleport(newLoc);
    }
    
    @Override
    public ServerWorld world() {
        return BukkitAdapter.adapt(delegate.getWorld());
    }
}
