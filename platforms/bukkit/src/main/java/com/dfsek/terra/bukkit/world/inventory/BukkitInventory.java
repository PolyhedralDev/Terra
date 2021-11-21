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

package com.dfsek.terra.bukkit.world.inventory;

import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.api.inventory.ItemStack;


public class BukkitInventory implements Inventory {
    private final org.bukkit.inventory.Inventory delegate;
    
    public BukkitInventory(org.bukkit.inventory.Inventory delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void setItem(int slot, ItemStack newStack) {
        delegate.setItem(slot, ((BukkitItemStack) newStack).getHandle());
    }
    
    @Override
    public int getSize() {
        return delegate.getSize();
    }
    
    @Override
    public ItemStack getItem(int slot) {
        org.bukkit.inventory.ItemStack itemStack = delegate.getItem(slot);
        return itemStack == null ? null : new BukkitItemStack(itemStack);
    }
    
    @Override
    public org.bukkit.inventory.Inventory getHandle() {
        return delegate;
    }
}
