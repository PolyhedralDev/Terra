package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.generic.inventory.item.ItemMeta;

public class BukkitItemMeta implements ItemMeta {
    private final org.bukkit.inventory.meta.ItemMeta delegate;

    public BukkitItemMeta(org.bukkit.inventory.meta.ItemMeta delegate) {
        this.delegate = delegate;
    }

    @Override
    public org.bukkit.inventory.meta.ItemMeta getHandle() {
        return delegate;
    }
}
