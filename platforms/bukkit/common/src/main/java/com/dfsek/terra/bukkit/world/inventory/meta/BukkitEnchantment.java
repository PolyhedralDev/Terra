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

package com.dfsek.terra.bukkit.world.inventory.meta;

import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemStack;


public class BukkitEnchantment implements Enchantment {
    private final org.bukkit.enchantments.Enchantment delegate;
    
    public BukkitEnchantment(org.bukkit.enchantments.Enchantment delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public org.bukkit.enchantments.Enchantment getHandle() {
        return delegate;
    }
    
    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return delegate.canEnchantItem(((BukkitItemStack) itemStack).getHandle());
    }
    
    @Override
    public boolean conflictsWith(Enchantment other) {
        return delegate.conflictsWith(((BukkitEnchantment) other).getHandle());
    }
    
    @Override
    public String getID() {
        return delegate.getKey().toString();
    }
    
    @Override
    public int getMaxLevel() {
        return delegate.getMaxLevel();
    }
}
