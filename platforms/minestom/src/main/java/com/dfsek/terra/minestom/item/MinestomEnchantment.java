package com.dfsek.terra.minestom.item;

import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.Enchantment;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.Material;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.RegistryKey;

import java.util.Objects;


public class MinestomEnchantment implements Enchantment {
    private final net.minestom.server.item.enchant.Enchantment delegate;
    private final String id;

    public MinestomEnchantment(net.minestom.server.item.enchant.Enchantment delegate) {
        this.delegate = delegate;
        DynamicRegistry<net.minestom.server.item.enchant.Enchantment> registry = MinecraftServer.getEnchantmentRegistry();
        this.id = Objects.requireNonNull(registry.getKey(delegate)).toString();
    }

    public MinestomEnchantment(String id) {
        Key key = Key.key(id);
        this.delegate = MinecraftServer.getEnchantmentRegistry().get(key);
        this.id = id;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return delegate.supportedItems().contains((Material) itemStack.getType().getHandle());
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        var otherDelegate = ((MinestomEnchantment) other).delegate;
        delegate.exclusiveSet();

        // Get the registry key for the other enchantment to use in contains
        try {
            DynamicRegistry<net.minestom.server.item.enchant.Enchantment> registry = MinecraftServer.getEnchantmentRegistry();
            RegistryKey<net.minestom.server.item.enchant.Enchantment> otherKey = registry.getKey(otherDelegate);
            return delegate.exclusiveSet().contains(otherKey);
        } catch (Exception e) {
            // If the key approach fails, fall back to a more basic implementation
            String otherId = ((MinestomEnchantment) other).id;
            return otherId.equals(this.id);
        }
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