package com.dfsek.terra.forge.inventory;

import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.ItemStack;

public class ForgeItem implements Item {
    private final net.minecraft.item.Item delegate;

    public ForgeItem(net.minecraft.item.Item delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.item.Item getHandle() {
        return delegate;
    }

    @Override
    public ItemStack newItemStack(int amount) {
        return new ForgeItemStack(new net.minecraft.item.ItemStack(delegate, amount));
    }

    @Override
    public double getMaxDurability() {
        return delegate.getMaxDamage();
    }
}
