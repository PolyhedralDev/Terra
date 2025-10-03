package com.dfsek.terra.minestom.item;

import net.minestom.server.item.Material;

import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.ItemStack;


public class MinestomMaterial implements Item {
    private final Material delegate;

    public MinestomMaterial(Material delegate) {
        this.delegate = delegate;
    }

    public MinestomMaterial(String id) {
        this.delegate = Material.fromId(Integer.parseInt(id));
    }

    @Override
    public ItemStack newItemStack(int amount) {
        return new MinestomItemStack(net.minestom.server.item.ItemStack.builder(delegate).amount(amount).build());
    }

    @Override
    public double getMaxDurability() {
        return 0;
    }

    @Override
    public Material getHandle() {
        return delegate;
    }
}