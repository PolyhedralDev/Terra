package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.api.platform.world.block.MaterialData;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;

public class BukkitItemStack implements ItemStack {
    protected org.bukkit.inventory.ItemStack delegate;

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
    public MaterialData getType() {
        return new BukkitMaterialData(delegate.getType());
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
        return new BukkitItemMeta(delegate.getItemMeta());
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
