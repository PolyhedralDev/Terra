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

package com.dfsek.terra.bukkit.world.block;

import org.bukkit.Material;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitBlockTypeAndItem implements BlockType, Item {
    private final Material delegate;
    
    public BukkitBlockTypeAndItem(Material delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public Material getHandle() {
        return delegate;
    }
    
    @Override
    public BlockState getDefaultState() {
        return BukkitAdapter.adapt(delegate.createBlockData());
    }
    
    @Override
    public boolean isSolid() {
        return delegate.isOccluding();
    }
    
    @Override
    public boolean isWater() {
        return delegate == Material.WATER;
    }
    
    @Override
    public ItemStack newItemStack(int amount) {
        return BukkitAdapter.adapt(new org.bukkit.inventory.ItemStack(delegate, amount));
    }
    
    @Override
    public double getMaxDurability() {
        return delegate.getMaxDurability();
    }
    
    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BukkitBlockTypeAndItem)) return false;
        return delegate == ((BukkitBlockTypeAndItem) obj).delegate;
    }
}
