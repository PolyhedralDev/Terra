package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;

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
        return new FabricItem(delegate.getItem());
    }

    @Override
    public ItemStack clone() {
        try {
            FabricItemStack stack = (FabricItemStack) super.clone();
            stack.delegate = delegate;
            return stack;
        } catch(CloneNotSupportedException e) {
            throw new Error();
        }
    }

    @Override
    public ItemMeta getItemMeta() {
        return null;
    }

    @Override
    public void setItemMeta(ItemMeta meta) {

    }

    @Override
    public net.minecraft.item.ItemStack getHandle() {
        return delegate;
    }
}
