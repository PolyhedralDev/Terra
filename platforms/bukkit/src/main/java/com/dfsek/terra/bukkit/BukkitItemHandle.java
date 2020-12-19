package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.platform.inventory.ItemHandle;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.world.block.MaterialData;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;

public class BukkitItemHandle implements ItemHandle {
    @Override
    public ItemStack newItemStack(MaterialData material, int amount) {
        return new BukkitItemStack(new org.bukkit.inventory.ItemStack(((BukkitMaterialData) material).getHandle(), amount));
    }
}
