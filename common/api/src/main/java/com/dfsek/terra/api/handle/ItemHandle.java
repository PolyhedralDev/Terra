/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.handle;

import java.util.Set;

import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;


public interface ItemHandle {
    
    Item createItem(String data);
    
    Enchantment getEnchantment(String id);
    
    Set<Enchantment> getEnchantments();
}
