package com.dfsek.terra.bukkit.world.inventory;

import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitItemStack implements ItemStack {
    private org.bukkit.inventory.ItemStack delegate;

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
    public ItemStack clone() {
        BukkitItemStack clone;
        try {
            clone = (BukkitItemStack) super.clone();
            clone.delegate = delegate.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
        return clone;
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
