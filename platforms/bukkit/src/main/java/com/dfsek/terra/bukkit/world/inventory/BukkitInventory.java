package com.dfsek.terra.bukkit.world.inventory;

import com.dfsek.terra.api.platform.inventory.Inventory;
import com.dfsek.terra.api.platform.inventory.ItemStack;

public class BukkitInventory implements Inventory {
    private final org.bukkit.inventory.Inventory delegate;

    public BukkitInventory(org.bukkit.inventory.Inventory delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    @Override
    public ItemStack getItem(int slot) {
        return new BukkitItemStack(delegate.getItem(slot));
    }

    @Override
    public void setItem(int slot, ItemStack newStack) {
        delegate.setItem(slot, ((BukkitItemStack) newStack).getHandle());
    }

    @Override
    public org.bukkit.inventory.Inventory getHandle() {
        return delegate;
    }
}
