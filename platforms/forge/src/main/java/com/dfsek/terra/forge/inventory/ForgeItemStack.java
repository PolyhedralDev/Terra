package com.dfsek.terra.forge.inventory;

import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.forge.inventory.meta.ForgeDamageable;
import com.dfsek.terra.forge.inventory.meta.ForgeItemMeta;

public class ForgeItemStack implements ItemStack {
    private net.minecraft.item.ItemStack delegate;

    public ForgeItemStack(net.minecraft.item.ItemStack delegate) {
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
        return new ForgeItem(delegate.getItem());
    }

    @Override
    public ItemMeta getItemMeta() {
        if(delegate.isDamageableItem()) return new ForgeDamageable(delegate.copy());
        return new ForgeItemMeta(delegate.copy());
    }

    @Override
    public void setItemMeta(ItemMeta meta) {
        this.delegate = ((ForgeItemMeta) meta).getHandle();
    }

    @Override
    public net.minecraft.item.ItemStack getHandle() {
        return delegate;
    }
}
