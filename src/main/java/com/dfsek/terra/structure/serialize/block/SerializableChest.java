package com.dfsek.terra.structure.serialize.block;

import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;

public class SerializableChest implements SerializableContainer {
    long serialVersionUID = 5298928608478640003L;
    @Override
    public Inventory getInventory(Inventory orig) {
        return null;
    }

    @Override
    public BlockState getState(BlockState orig) {
        return null;
    }
}
