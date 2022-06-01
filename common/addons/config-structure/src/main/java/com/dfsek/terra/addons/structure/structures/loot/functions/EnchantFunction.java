/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.structures.loot.functions;

import net.jafama.FastMath;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.inventory.item.ItemMeta;


public class EnchantFunction implements LootFunction {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnchantFunction.class);
    private final int min;
    private final int max;
    private final JSONArray disabled;
    private final Platform platform;
    
    
    public EnchantFunction(int min, int max, JSONArray disabled, Platform platform) {
        this.max = max;
        this.min = min;
        this.disabled = disabled;
        this.platform = platform;
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
        if(original.getItemMeta() == null) return original;
        
        double enchant = (r.nextDouble() * (max - min)) + min;
        List<Enchantment> possible = new ArrayList<>();
        for(Enchantment ench : platform.getItemHandle().getEnchantments()) {
            if(ench.canEnchantItem(original) && (disabled == null || !this.disabled.contains(ench.getID()))) {
                possible.add(ench);
            }
        }
        int numEnchant = (r.nextInt((int) FastMath.abs(enchant)) / 10 + 1);
        Collections.shuffle(possible);
        ItemMeta meta = original.getItemMeta();
        iter:
        for(int i = 0; i < numEnchant && i < possible.size(); i++) {
            Enchantment chosen = possible.get(i);
            for(Enchantment ench : meta.getEnchantments().keySet()) {
                if(chosen.conflictsWith(ench)) continue iter;
            }
            int lvl = r.nextInt(1 + (int) (((enchant / 40 > 1) ? 1 : enchant / 40) * (chosen.getMaxLevel())));
            try {
                meta.addEnchantment(chosen, FastMath.max(lvl, 1));
            } catch(IllegalArgumentException e) {
                LOGGER.warn(
                        "Attempted to enchant {} with {} at level {}, but an unexpected exception occurred! Usually this is caused by a " +
                        "misbehaving enchantment plugin.",
                        original.getType(), chosen, FastMath.max(lvl, 1));
            }
        }
        original.setItemMeta(meta);
        return original;
    }
}
