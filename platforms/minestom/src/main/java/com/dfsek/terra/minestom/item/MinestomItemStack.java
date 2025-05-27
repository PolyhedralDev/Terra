package com.dfsek.terra.minestom.item;

import com.dfsek.terra.api.inventory.Item;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;

import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.DynamicRegistry.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;


public class MinestomItemStack implements com.dfsek.terra.api.inventory.ItemStack {
    private ItemStack base;

    public MinestomItemStack(net.minestom.server.item.@NotNull ItemStack base) {
        this.base = base;
    }

    @Override
    public Object getHandle() {
        return base;
    }

    @Override
    public int getAmount() {
        return base.amount();
    }

    @Override
    public void setAmount(int i) {
        base = base.withAmount(i);
    }

    @Override
    public Item getType() {
        return new MinestomMaterial(base.material());
    }

    @Override
    public ItemMeta getItemMeta() {
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        EnchantmentList enchantmentList = base.get(DataComponents.ENCHANTMENTS);
        if(enchantmentList != null) {
            enchantmentList.enchantments().forEach((enchantmentKey, integer) -> {
                enchantments.put(
                    new MinestomEnchantment(Objects.requireNonNull(MinecraftServer.getEnchantmentRegistry().get(enchantmentKey))), integer);
            });
        }
        return new MinestomItemMeta(enchantments);
    }

    @Override
    public void setItemMeta(ItemMeta meta) {
        HashMap<Key<net.minestom.server.item.enchant.Enchantment>, Integer> enchantments = new HashMap<>();
        DynamicRegistry<net.minestom.server.item.enchant.Enchantment> registry = MinecraftServer.getEnchantmentRegistry();
        meta.getEnchantments().forEach((key, value) -> {
            MinestomEnchantment enchantment = (MinestomEnchantment) key;
            enchantments.put(registry.getKey(enchantment.getHandle()), value);
        });

        EnchantmentList list = new EnchantmentList(enchantments);
        base = base.with(DataComponents.ENCHANTMENTS, list);
    }
}
