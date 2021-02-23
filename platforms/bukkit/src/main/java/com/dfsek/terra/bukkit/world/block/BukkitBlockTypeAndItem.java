package com.dfsek.terra.bukkit.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.inventory.Item;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import org.bukkit.Material;

public class BukkitBlockTypeAndItem implements BlockType, Item {
    private final Material delegate;

    public BukkitBlockTypeAndItem(Material delegate) {
        this.delegate = delegate;
    }

    @Override
    public Material getHandle() {
        return delegate;
    }

    @Override
    public BlockData getDefaultData() {
        return BukkitAdapter.adapt(delegate.createBlockData());
    }

    @Override
    public boolean isSolid() {
        return delegate.isSolid();
    }

    @Override
    public ItemStack newItemStack(int amount) {
        return BukkitAdapter.adapt(new org.bukkit.inventory.ItemStack(delegate, amount));
    }

    @Override
    public double getMaxDurability() {
        return delegate.getMaxDurability();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BukkitBlockTypeAndItem)) return false;
        return delegate == ((BukkitBlockTypeAndItem) obj).delegate;
    }
}
