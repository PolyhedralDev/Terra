package com.dfsek.terra.minestom.item;

import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;

import java.util.HashMap;
import java.util.Map;


public class MinestomItemMeta implements ItemMeta {
    private final HashMap<Enchantment, Integer> enchantments;

    public MinestomItemMeta(HashMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    @Override
    public void addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    @Override
    public Object getHandle() {
        return enchantments;
    }
}
