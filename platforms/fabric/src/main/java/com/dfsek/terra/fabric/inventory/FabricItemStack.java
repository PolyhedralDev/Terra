package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.generic.inventory.ItemStack;
import com.dfsek.terra.api.generic.inventory.item.ItemMeta;
import com.dfsek.terra.api.generic.world.block.MaterialData;

public class FabricItemStack implements ItemStack {
    net.minecraft.item.ItemStack delegate;

    @Override
    public int getAmount() {
        return delegate.getCount();
    }

    @Override
    public void setAmount(int i) {
        delegate.setCount(i);
    }

    @Override
    public MaterialData getType() {
        return null;
    }

    @Override
    public ItemStack clone() {
        return null;
    }

    @Override
    public ItemMeta getItemMeta() {
        return null;
    }

    @Override
    public void setItemMeta(ItemMeta meta) {

    }

    @Override
    public Object getHandle() {
        return delegate;
    }
}
