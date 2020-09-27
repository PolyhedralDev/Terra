package com.dfsek.terra.structure.serialize.block;

import org.bukkit.inventory.Inventory;

public interface SerializableContainer extends SerializableBlockState {
    long serialVersionUID = 5298928608478640002L;
    Inventory getInventory(Inventory orig);
}
