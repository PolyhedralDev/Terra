package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.world.block.MaterialData;

public interface ItemHandle {
    ItemStack newItemStack(MaterialData material, int amount);
}
