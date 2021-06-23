package com.dfsek.terra.api.inventory;

import com.dfsek.terra.api.Handle;

public interface Inventory extends Handle {
    int getSize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack newStack);
}
