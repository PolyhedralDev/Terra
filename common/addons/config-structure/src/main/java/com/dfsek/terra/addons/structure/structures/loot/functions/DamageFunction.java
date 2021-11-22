/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.structures.loot.functions;

import java.util.Random;

import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.Damageable;
import com.dfsek.terra.api.inventory.item.ItemMeta;


/**
 * Loot LootFunction for setting the damage on items in Loot Tables
 */
public class DamageFunction implements LootFunction {
    private final int max;
    private final int min;
    
    /**
     * Instantiates a DamageFunction.
     *
     * @param min Minimum amount of damage (percentage, out of 100)
     * @param max Maximum amount of damage (percentage, out of 100)
     */
    public DamageFunction(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    /**
     * Applies the function to an ItemStack.
     *
     * @param original The ItemStack on which to apply the function.
     * @param r        The Random instance to use.
     *
     * @return - ItemStack - The mutated ItemStack.
     */
    @Override
    public ItemStack apply(ItemStack original, Random r) {
        if(original == null) return null;
        if(!original.isDamageable()) return original;
        ItemMeta meta = original.getItemMeta();
        double itemDurability = (r.nextDouble() * (max - min)) + min;
        Damageable damage = (Damageable) meta;
        damage.setDamage((int) (original.getType().getMaxDurability() - (itemDurability / 100) * original.getType().getMaxDurability()));
        original.setItemMeta((ItemMeta) damage);
        return original;
    }
}
