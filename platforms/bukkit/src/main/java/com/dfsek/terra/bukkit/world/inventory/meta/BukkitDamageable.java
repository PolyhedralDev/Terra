package com.dfsek.terra.bukkit.world.inventory.meta;

import com.dfsek.terra.api.inventory.item.Damageable;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BukkitDamageable extends BukkitItemMeta implements Damageable {
    public BukkitDamageable(org.bukkit.inventory.meta.Damageable delegate) {
        super((ItemMeta) delegate);
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
