package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.fabric.inventory.meta.FabricDamageable;
import com.dfsek.terra.fabric.inventory.meta.FabricItemMeta;

public class FabricItemStack implements ItemStack {
    private net.minecraft.item.ItemStack delegate;

    public FabricItemStack(net.minecraft.item.ItemStack delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getAmount() {
        return delegate.getCount();
    }

    @Override
    public void setAmount(int i) {
        delegate.setCount(i);
    }

    @Override
    public Item getType() {
        return (Item) delegate.getItem();
    }

    @Override
    public ItemMeta getItemMeta() {
        if(delegate.isDamageable()) return new FabricDamageable(delegate.copy());
        return new FabricItemMeta(delegate.copy());
    }

    @Override
    public void setItemMeta(ItemMeta meta) {
        this.delegate = ((FabricItemMeta) meta).getHandle();
    }

    @Override
    public net.minecraft.item.ItemStack getHandle() {
        return delegate;
    }
}
