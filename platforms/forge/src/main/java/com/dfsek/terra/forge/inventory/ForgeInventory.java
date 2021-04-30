package com.dfsek.terra.forge.inventory;

import com.dfsek.terra.api.platform.inventory.Inventory;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.forge.world.ForgeAdapter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Items;

public class ForgeInventory implements Inventory {
    private final net.minecraft.inventory.IInventory delegate;

    public ForgeInventory(IInventory delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.inventory.IInventory getHandle() {
        return delegate;
    }

    @Override
    public int getSize() {
        return delegate.getContainerSize();
    }

    @Override
    public ItemStack getItem(int slot) {
        net.minecraft.item.ItemStack itemStack = delegate.getItem(slot);
        return itemStack.getItem() == Items.AIR ? null : ForgeAdapter.adapt(itemStack);
    }

    @Override
    public void setItem(int slot, ItemStack newStack) {
        delegate.setItem(slot, ForgeAdapter.adapt(newStack));
    }
}
