package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.inventory.ItemHandle;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.world.block.MaterialData;

public class FabricItemHandle implements ItemHandle {
    @Override
    public ItemStack newItemStack(MaterialData material, int amount) {
        return null;
    }
}
