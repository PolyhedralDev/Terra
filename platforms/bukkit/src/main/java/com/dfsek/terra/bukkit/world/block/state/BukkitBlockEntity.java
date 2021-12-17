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

package com.dfsek.terra.bukkit.world.block.state;

import org.bukkit.block.Container;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;


public class BukkitBlockEntity implements BlockEntity {
    private final org.bukkit.block.BlockState delegate;
    
    protected BukkitBlockEntity(org.bukkit.block.BlockState block) {
        this.delegate = block;
    }
    
    public static BukkitBlockEntity newInstance(org.bukkit.block.BlockState block) {
        if(block instanceof Container) return new BukkitContainer((Container) block);
        if(block instanceof Sign) return new BukkitSign((Sign) block);
        if(block instanceof CreatureSpawner) return new BukkitMobSpawner((CreatureSpawner) block);
        return new BukkitBlockEntity(block);
    }
    
    @Override
    public org.bukkit.block.BlockState getHandle() {
        return delegate;
    }
    
    @Override
    public boolean update(boolean applyPhysics) {
        return delegate.update(true, applyPhysics);
    }
    
    @Override
    public Vector3 getPosition() {
        return BukkitAdapter.adapt(delegate.getBlock().getLocation().toVector());
    }
    
    @Override
    public int getX() {
        return delegate.getX();
    }
    
    @Override
    public int getY() {
        return delegate.getY();
    }
    
    @Override
    public int getZ() {
        return delegate.getZ();
    }
    
    @Override
    public BlockState getBlockState() {
        return BukkitBlockState.newInstance(delegate.getBlockData());
    }
}
