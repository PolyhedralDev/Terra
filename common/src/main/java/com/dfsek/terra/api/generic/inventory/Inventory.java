package com.dfsek.terra.api.generic.inventory;

import com.dfsek.terra.api.generic.Handle;

public interface Inventory extends Handle {
    int getSize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack newStack);
}
