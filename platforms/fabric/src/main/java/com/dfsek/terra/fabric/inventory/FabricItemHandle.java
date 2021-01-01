package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.inventory.ItemStack;

public class FabricItemHandle implements ItemHandle {
    @Override
    public ItemStack newItemStack(MaterialData material, int amount) {
        return null;
    }
}
