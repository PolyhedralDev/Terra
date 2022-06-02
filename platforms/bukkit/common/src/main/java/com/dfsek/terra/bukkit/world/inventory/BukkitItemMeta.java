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

import org.bukkit.inventory.meta.Damageable;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.inventory.meta.BukkitDamageable;
import com.dfsek.terra.bukkit.world.inventory.meta.BukkitEnchantment;


public class BukkitItemMeta implements ItemMeta {
    private final org.bukkit.inventory.meta.ItemMeta delegate;
    
    protected BukkitItemMeta(org.bukkit.inventory.meta.ItemMeta delegate) {
        this.delegate = delegate;
    }
    
    public static BukkitItemMeta newInstance(org.bukkit.inventory.meta.ItemMeta delegate) {
        if(delegate instanceof Damageable) return new BukkitDamageable((Damageable) delegate);
        return new BukkitItemMeta(delegate);
    }
    
    @Override
    public org.bukkit.inventory.meta.ItemMeta getHandle() {
        return delegate;
    }
    
    @Override
    public void addEnchantment(Enchantment enchantment, int level) {
        delegate.addEnchant(((BukkitEnchantment) enchantment).getHandle(), level, true);
    }
    
    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        Map<Enchantment, Integer> map = new HashMap<>();
        delegate.getEnchants().forEach((enchantment, integer) -> map.put(BukkitAdapter.adapt(enchantment), integer));
        return map;
    }
}
