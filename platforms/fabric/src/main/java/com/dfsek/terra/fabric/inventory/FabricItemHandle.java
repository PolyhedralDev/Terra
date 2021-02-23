package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;

import java.util.Collections;
import java.util.Set;

public class FabricItemHandle implements ItemHandle {

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
        return Collections.emptySet();
    }
}
