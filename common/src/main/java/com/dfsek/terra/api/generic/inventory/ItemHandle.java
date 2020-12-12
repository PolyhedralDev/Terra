package com.dfsek.terra.api.generic.inventory;

import com.dfsek.terra.api.generic.world.block.MaterialData;

public interface ItemHandle {
    ItemStack newItemStack(MaterialData material, int amount);
}
