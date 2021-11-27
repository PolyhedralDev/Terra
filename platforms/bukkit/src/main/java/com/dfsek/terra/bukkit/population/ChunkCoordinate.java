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

package com.dfsek.terra.bukkit.population;


import java.io.Serializable;
import java.util.UUID;

import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.bukkit.world.BukkitWorld;


public class ChunkCoordinate implements Serializable {
    public static final long serialVersionUID = 7102462856296750285L;
    private final int x;
    private final int z;
    private final UUID worldID;
    
    public ChunkCoordinate(int x, int z, UUID worldID) {
        this.x = x;
        this.z = z;
        this.worldID = worldID;
    }
    
    public ChunkCoordinate(Chunk c) {
        this.x = c.getX();
        this.z = c.getZ();
        this.worldID = ((BukkitWorld) c.getWorld()).getUID();
    }
    
    public UUID getWorldID() {
        return worldID;
    }
    
    public int getX() {
        return x;
    }
    
    public int getZ() {
        return z;
    }
    
    @Override
    public int hashCode() {
        return x * 31 + z;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ChunkCoordinate other)) return false;
        return other.getX() == x && other.getZ() == z;
    }
}
