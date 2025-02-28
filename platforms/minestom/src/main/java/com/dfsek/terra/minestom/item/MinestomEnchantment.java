package com.dfsek.terra.minestom.item;

import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.Enchantment;

import net.minestom.server.MinecraftServer;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;

import java.util.Objects;


public class MinestomEnchantment implements Enchantment {
    private final net.minestom.server.item.enchant.Enchantment delegate;
    private final String id;

    public MinestomEnchantment(net.minestom.server.item.enchant.Enchantment delegate) {
        this.delegate = delegate;
        id = Objects.requireNonNull(delegate.registry()).raw();
    }

    public MinestomEnchantment(String id) {
        this.delegate = MinecraftServer.getEnchantmentRegistry().get(NamespaceID.from(id));
        this.id = id;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return delegate.supportedItems().contains((Material) itemStack.getType().getHandle());
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return delegate.exclusiveSet().contains(NamespaceID.from(((MinestomEnchantment) other).id));
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public int getMaxLevel() {
        return delegate.maxLevel();
    }

    @Override
    public net.minestom.server.item.enchant.Enchantment getHandle() {
        return delegate;
    }
}
