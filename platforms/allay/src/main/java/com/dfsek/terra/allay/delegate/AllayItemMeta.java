package com.dfsek.terra.allay.delegate;

import org.allaymc.api.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;

import org.allaymc.api.item.enchantment.EnchantmentInstance;
import org.allaymc.api.item.enchantment.EnchantmentType;


/**
 * @author daoge_cmd
 */
public record AllayItemMeta(ItemStack allayItemStack) implements ItemMeta {
    @Override
    public void addEnchantment(Enchantment enchantment, int level) {
        EnchantmentType allayEnchantment = ((AllayEnchantment) enchantment).allayEnchantment();
        allayItemStack.addEnchantment(allayEnchantment, (short) level);
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        Map<Enchantment, Integer> results = new HashMap<>();
        for (EnchantmentInstance allayEnchantmentInstance : allayItemStack.getEnchantments()) {
            results.put(new AllayEnchantment(allayEnchantmentInstance.getType()), allayEnchantmentInstance.getLevel());
        }
        return results;
    }

    @Override
    public ItemStack getHandle() {
        return allayItemStack;
    }
}
