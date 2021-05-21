package com.dfsek.terra.api.platform.handle;

import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;

import java.util.Set;

public interface ItemHandle {

    Item createItem(String data);

    Enchantment getEnchantment(String id);

    Set<Enchantment> getEnchantments();
}
