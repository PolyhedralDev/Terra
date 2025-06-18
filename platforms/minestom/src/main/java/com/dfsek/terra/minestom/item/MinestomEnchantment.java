package com.dfsek.terra.minestom.item;

import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.Enchantment;

import net.minestom.server.MinecraftServer;
import net.minestom.server.item.Material;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.RegistryKey;


public class MinestomEnchantment implements Enchantment {
    private final net.minestom.server.item.enchant.Enchantment registryItem;
    private final RegistryKey<net.minestom.server.item.enchant.Enchantment> id;
    private static final DynamicRegistry<net.minestom.server.item.enchant.Enchantment> enchantmentRegistry =
        MinecraftServer.getEnchantmentRegistry();

    public MinestomEnchantment(RegistryKey<net.minestom.server.item.enchant.Enchantment> id) {
        this.id = id;
        this.registryItem = enchantmentRegistry.get(id);
    }

    public MinestomEnchantment(String id) {
        this(RegistryKey.unsafeOf(id));
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return registryItem.supportedItems().contains((Material) itemStack.getType().getHandle());
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return registryItem.exclusiveSet().contains(((MinestomEnchantment) other).id);
    }

    @Override
    public String getID() {
        return id.name();
    }

    @Override
    public int getMaxLevel() {
        return registryItem.maxLevel();
    }

    @Override
    public net.minestom.server.item.enchant.Enchantment getHandle() {
        return registryItem;
    }
}
