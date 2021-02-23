package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;

public interface ItemStack extends Handle, Cloneable {
    int getAmount();

    void setAmount(int i);

    Item getType();

    ItemStack clone();

    ItemMeta getItemMeta();

    void setItemMeta(ItemMeta meta);
}
