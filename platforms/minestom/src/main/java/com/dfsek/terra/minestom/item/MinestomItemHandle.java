package com.dfsek.terra.minestom.item;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;

import java.util.Set;


public class MinestomItemHandle implements ItemHandle {
    @Override
    public Item createItem(String data) {
        return null;
    }

    @Override
    public Enchantment getEnchantment(String id) {
        return null;
    }

    @Override
    public Set<Enchantment> getEnchantments() {
        return Set.of();
    }
}
