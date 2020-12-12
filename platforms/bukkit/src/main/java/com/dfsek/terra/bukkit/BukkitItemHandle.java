package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.generic.inventory.ItemHandle;
import com.dfsek.terra.api.generic.inventory.ItemStack;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;

public class BukkitItemHandle implements ItemHandle {
    @Override
    public ItemStack newItemStack(MaterialData material, int amount) {
        return new BukkitItemStack(new org.bukkit.inventory.ItemStack(((BukkitMaterialData) material).getHandle(), amount));
    }
}
