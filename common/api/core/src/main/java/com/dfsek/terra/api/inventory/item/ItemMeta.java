/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.inventory.item;

import java.util.Map;

import com.dfsek.terra.api.Handle;


public interface ItemMeta extends Handle {
    void addEnchantment(Enchantment enchantment, int level);
    
    Map<Enchantment, Integer> getEnchantments();
}
