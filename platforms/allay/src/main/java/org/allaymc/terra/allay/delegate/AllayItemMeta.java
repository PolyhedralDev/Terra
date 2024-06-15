package org.allaymc.terra.allay.delegate;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;

import org.allaymc.api.item.ItemStack;

import java.util.HashMap;
import java.util.Map;


/**
 * Terra Project 2024/6/16
 *
 * 物品元数据。在allay中物品元数据没有单独的类，故直接使用ItemStack代替
 *
 * @author daoge_cmd
 */
public record AllayItemMeta(ItemStack allayItemStack) implements ItemMeta {
    @Override
    public void addEnchantment(Enchantment enchantment, int level) {
        var allayEnchantment = ((AllayEnchantment) enchantment).allayEnchantment();
        allayItemStack.addEnchantment(allayEnchantment, (short) level);
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        Map<Enchantment, Integer> results = new HashMap<>();
        for (var allayEnchantmentInstance : allayItemStack.getEnchantments()) {
            results.put(new AllayEnchantment(allayEnchantmentInstance.getType()), (int) allayEnchantmentInstance.getLevel());
        }
        return results;
    }

    @Override
    public ItemStack getHandle() {
        return allayItemStack;
    }
}
