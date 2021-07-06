package com.dfsek.terra.bukkit.world.block.state;

import com.dfsek.terra.api.block.state.Container;
import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.bukkit.world.inventory.BukkitInventory;

public class BukkitContainer extends BukkitBlockState implements Container {

    protected BukkitContainer(org.bukkit.block.Container block) {
        super(block);
    }

    @Override
    public Inventory getInventory() {
        return new BukkitInventory(((org.bukkit.block.Container) getHandle()).getInventory());
    }

    @Override
    public boolean update(boolean applyPhysics) {
        return false; // This clears the inventory. we don't want that.
    }
}
