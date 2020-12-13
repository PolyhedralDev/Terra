package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.generic.inventory.ItemHandle;
import com.dfsek.terra.api.generic.inventory.ItemStack;
import com.dfsek.terra.api.generic.world.block.MaterialData;

public class FabricItemHandle implements ItemHandle {
    @Override
    public ItemStack newItemStack(MaterialData material, int amount) {
        return null;
    }
}
