package com.dfsek.terra.structure.serialize;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class SerializableInventory implements Serializable {
    public static final long serialVersionUID = 5298928608478640004L;
    private SerializableItemStack[] items;
    public SerializableInventory(Inventory inv) {
        items = new SerializableItemStack[inv.getSize()];
        for(int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if(item == null) return;
            items[i] = new SerializableItemStack(item);
        }
    }
}
