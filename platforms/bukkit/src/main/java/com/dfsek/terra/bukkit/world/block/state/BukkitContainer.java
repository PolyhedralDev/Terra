package com.dfsek.terra.bukkit.world.block.state;

import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.platform.inventory.Inventory;
import com.dfsek.terra.bukkit.world.inventory.BukkitInventory;

public class BukkitContainer extends BukkitBlockState implements Container {

    protected BukkitContainer(org.bukkit.block.Container block) {
        super(block);
    }

    @Override
    public Inventory getInventory() {
        return new BukkitInventory(((org.bukkit.block.Container) getHandle()).getInventory());
    }

}
