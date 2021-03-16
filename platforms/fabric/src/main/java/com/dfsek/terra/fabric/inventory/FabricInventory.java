package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.inventory.Inventory;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.item.Items;

public class FabricInventory implements Inventory {
    private final net.minecraft.inventory.Inventory delegate;

    public FabricInventory(net.minecraft.inventory.Inventory delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.inventory.Inventory getHandle() {
        return delegate;
    }

    @Override
    public int getSize() {
        return delegate.size();
    }

    @Override
    public ItemStack getItem(int slot) {
        net.minecraft.item.ItemStack itemStack = delegate.getStack(slot);
        return itemStack.getItem() == Items.AIR ? null : FabricAdapter.adapt(itemStack);
    }

    @Override
    public void setItem(int slot, ItemStack newStack) {
        delegate.setStack(slot, FabricAdapter.adapt(newStack));
    }
}
