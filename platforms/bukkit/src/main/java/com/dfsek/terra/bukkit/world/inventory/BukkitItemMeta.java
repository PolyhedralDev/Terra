package com.dfsek.terra.bukkit.world.inventory;

import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.bukkit.world.inventory.meta.BukkitDamageable;
import org.bukkit.inventory.meta.Damageable;

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
}
