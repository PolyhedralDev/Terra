package com.dfsek.terra.api.generic.inventory;

import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.api.generic.inventory.item.ItemMeta;
import com.dfsek.terra.api.generic.world.block.MaterialData;

public interface ItemStack extends Handle, Cloneable {
    int getAmount();

    void setAmount(int i);

    MaterialData getType();

    ItemStack clone();

    ItemMeta getItemMeta();

    void setItemMeta(ItemMeta meta);
}
