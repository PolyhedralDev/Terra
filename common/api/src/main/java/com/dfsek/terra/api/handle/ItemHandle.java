package com.dfsek.terra.api.handle;

import java.util.Set;

import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;


public interface ItemHandle {
    
    Item createItem(String data);
    
    Enchantment getEnchantment(String id);
    
    Set<Enchantment> getEnchantments();
}
