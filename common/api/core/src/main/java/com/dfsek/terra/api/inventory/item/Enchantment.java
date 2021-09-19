package com.dfsek.terra.api.inventory.item;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.inventory.ItemStack;


public interface Enchantment extends Handle {
    boolean canEnchantItem(ItemStack itemStack);
    
    boolean conflictsWith(Enchantment other);
    
    String getID();
    
    int getMaxLevel();
}
