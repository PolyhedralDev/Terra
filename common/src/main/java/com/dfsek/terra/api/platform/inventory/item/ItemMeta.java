package com.dfsek.terra.api.platform.inventory.item;

import com.dfsek.terra.api.platform.Handle;

import java.util.Map;

public interface ItemMeta extends Handle {
    Map<Enchantment, Integer> getEnchantments();

    void addEnchantment(Enchantment enchantment, int level);
}
