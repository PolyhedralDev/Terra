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

package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitBlockState implements BlockState {
    private org.bukkit.block.data.BlockData delegate;
    
    protected BukkitBlockState(org.bukkit.block.data.BlockData delegate) {
        this.delegate = delegate;
    }
    
    public static BlockState newInstance(org.bukkit.block.data.BlockData bukkitData) {
        return new BukkitBlockState(bukkitData);
    }
    
    
    @Override
    public org.bukkit.block.data.BlockData getHandle() {
        return delegate;
    }
    
    @Override
    public boolean matches(BlockState data) {
        return delegate.getMaterial() == ((BukkitBlockState) data).getHandle().getMaterial();
    }
    
    @Override
    public <T> boolean has(Property<T> property) {
        return false;
    }
    
    @Override
    public <T> T get(Property<T> property) {
        return null;
    }
    
    @Override
    public <T> BlockState set(Property<T> property, T value) {
        return null;
    }
    
    @Override
    public BlockType getBlockType() {
        return BukkitAdapter.adapt(delegate.getMaterial());
    }
    
    @Override
    public String getAsString() {
        return delegate.getAsString(false);
    }
    
    @Override
    public boolean isAir() {
        return delegate.getMaterial().isAir();
    }

    @Override
    public BukkitBlockState clone() {
        try {
            BukkitBlockState n = (BukkitBlockState) super.clone();
            n.delegate = delegate.clone();
            return n;
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
