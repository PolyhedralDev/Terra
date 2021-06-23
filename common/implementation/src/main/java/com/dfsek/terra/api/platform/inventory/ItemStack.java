package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.inventory.item.Damageable;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;

public interface ItemStack extends Handle {
    int getAmount();

    void setAmount(int i);

    Item getType();

    ItemMeta getItemMeta();

    void setItemMeta(ItemMeta meta);

    default boolean isDamageable() {
        return getItemMeta() instanceof Damageable;
    }
}
