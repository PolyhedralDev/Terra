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

package com.dfsek.terra.bukkit.world.entity;

import com.dfsek.terra.api.entity.EntityType;


public class BukkitEntityType implements EntityType {
    private final org.bukkit.entity.EntityType delegate;
    
    public BukkitEntityType(org.bukkit.entity.EntityType delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public org.bukkit.entity.EntityType getHandle() {
        return delegate;
    }
}
