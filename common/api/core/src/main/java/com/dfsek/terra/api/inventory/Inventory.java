package com.dfsek.terra.api.inventory;

import com.dfsek.terra.api.Handle;


public interface Inventory extends Handle {
    void setItem(int slot, ItemStack newStack);
    
    int getSize();
    
    ItemStack getItem(int slot);
}
