package com.dfsek.terra.api.inventory;

import com.dfsek.terra.api.Handle;


/**
 * An inventory item.
 */
public interface Item extends Handle {
    ItemStack newItemStack(int amount);
    
    double getMaxDurability();
}
