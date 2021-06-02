package com.dfsek.terra.sponge.handle;

import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;

import java.util.Set;

public class SpongeItemHandle implements ItemHandle {
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
        return null;
    }
}
