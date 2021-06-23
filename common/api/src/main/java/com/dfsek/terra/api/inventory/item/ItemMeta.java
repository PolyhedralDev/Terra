package com.dfsek.terra.api.inventory.item;

import com.dfsek.terra.api.Handle;

import java.util.Map;

public interface ItemMeta extends Handle {
    Map<Enchantment, Integer> getEnchantments();

    void addEnchantment(Enchantment enchantment, int level);
}
