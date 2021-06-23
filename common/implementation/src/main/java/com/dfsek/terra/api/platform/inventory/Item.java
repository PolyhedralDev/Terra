package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;

/**
 * An inventory item.
 */
public interface Item extends Handle {
    ItemStack newItemStack(int amount);

    double getMaxDurability();
}
