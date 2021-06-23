package com.dfsek.terra.api.handle;

import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;

import java.util.Set;

public interface ItemHandle {

    Item createItem(String data);

    Enchantment getEnchantment(String id);

    Set<Enchantment> getEnchantments();
}
