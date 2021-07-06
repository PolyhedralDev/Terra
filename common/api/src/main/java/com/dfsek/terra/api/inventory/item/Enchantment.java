package com.dfsek.terra.api.inventory.item;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.inventory.ItemStack;

public interface Enchantment extends Handle {
    boolean canEnchantItem(ItemStack itemStack);

    String getID();

    boolean conflictsWith(Enchantment other);

    int getMaxLevel();
}
