package com.dfsek.terra.structure;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class StructureContainedInventory implements Serializable {
    public static final long serialVersionUID = - 175339605585943678L;
    private final int uid;
    private final int x, y, z;

    public StructureContainedInventory(Inventory orig, StructureContainedBlock link) {
        ItemStack stack = orig.getItem(0);
        x = link.getX();
        y = link.getY();
        z = link.getZ();
        if(stack == null) {
            uid = 0;
            return;
        }
        uid = stack.getAmount();
    }

    public int getUid() {
        return uid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
