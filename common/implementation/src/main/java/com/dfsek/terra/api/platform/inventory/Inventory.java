package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;

public interface Inventory extends Handle {
    int getSize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack newStack);
}
