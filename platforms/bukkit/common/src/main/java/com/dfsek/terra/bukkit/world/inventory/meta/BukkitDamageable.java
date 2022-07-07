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

import com.dfsek.terra.api.inventory.item.Damageable;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemMeta;


public class BukkitDamageable extends BukkitItemMeta implements Damageable {
    public BukkitDamageable(org.bukkit.inventory.meta.Damageable delegate) {
        super(delegate);
    }
    
    @Override
    public int getDamage() {
        return ((org.bukkit.inventory.meta.Damageable) getHandle()).getDamage();
    }
    
    @Override
    public void setDamage(int damage) {
        ((org.bukkit.inventory.meta.Damageable) getHandle()).setDamage(damage);
    }
    
    @Override
    public boolean hasDamage() {
        return ((org.bukkit.inventory.meta.Damageable) getHandle()).hasDamage();
    }
}
