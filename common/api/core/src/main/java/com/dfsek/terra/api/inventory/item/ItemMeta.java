package com.dfsek.terra.api.inventory.item;

import java.util.Map;

import com.dfsek.terra.api.Handle;


public interface ItemMeta extends Handle {
    void addEnchantment(Enchantment enchantment, int level);
    
    Map<Enchantment, Integer> getEnchantments();
}
