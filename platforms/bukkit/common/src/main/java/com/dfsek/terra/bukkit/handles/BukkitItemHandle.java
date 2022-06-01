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

package com.dfsek.terra.bukkit.handles;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.bukkit.util.MinecraftUtils;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitItemHandle implements ItemHandle {
    
    @Override
    public Item createItem(String data) {
        return BukkitAdapter.adapt(Material.matchMaterial(data));
    }
    
    @Override
    public Enchantment getEnchantment(String id) {
        return BukkitAdapter.adapt(
                org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft(MinecraftUtils.stripMinecraftNamespace(id))));
    }
    
    @Override
    public Set<Enchantment> getEnchantments() {
        return Arrays.stream(org.bukkit.enchantments.Enchantment.values()).map(BukkitAdapter::adapt).collect(Collectors.toSet());
    }
}
