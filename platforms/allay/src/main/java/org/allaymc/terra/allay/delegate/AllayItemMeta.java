package org.allaymc.terra.allay.delegate;

import org.allaymc.api.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;

/**
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
