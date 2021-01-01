package com.dfsek.terra.api.platform.handle;

import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.inventory.ItemStack;

public interface ItemHandle {
    ItemStack newItemStack(MaterialData material, int amount);
}
