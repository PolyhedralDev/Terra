/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.inventory.item;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.inventory.ItemStack;


public interface Enchantment extends Handle {
    boolean canEnchantItem(ItemStack itemStack);
    
    boolean conflictsWith(Enchantment other);
    
    String getID();
    
    int getMaxLevel();
}
