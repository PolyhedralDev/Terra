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

import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.ItemMeta;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitItemStack implements ItemStack {
    private final org.bukkit.inventory.ItemStack delegate;
    
    public BukkitItemStack(org.bukkit.inventory.ItemStack delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public int getAmount() {
        return delegate.getAmount();
    }
    
    @Override
    public void setAmount(int i) {
        delegate.setAmount(i);
    }
    
    @Override
    public Item getType() {
        return BukkitAdapter.adapt(delegate.getType());
    }
    
    @Override
    public ItemMeta getItemMeta() {
        return BukkitItemMeta.newInstance(delegate.getItemMeta());
    }
    
    @Override
    public void setItemMeta(ItemMeta meta) {
        delegate.setItemMeta(((BukkitItemMeta) meta).getHandle());
    }
    
    @Override
    public org.bukkit.inventory.ItemStack getHandle() {
        return delegate;
    }
}
