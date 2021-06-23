package com.dfsek.terra.api.platform.inventory.item;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.inventory.ItemStack;

public interface Enchantment extends Handle {
    boolean canEnchantItem(ItemStack itemStack);

    String getID();

    boolean conflictsWith(Enchantment other);

    int getMaxLevel();
}
