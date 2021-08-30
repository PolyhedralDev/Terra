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
