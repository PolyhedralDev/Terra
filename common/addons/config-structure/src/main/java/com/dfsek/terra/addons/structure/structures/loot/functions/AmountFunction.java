/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.structures.loot.functions;


import java.util.Random;

import com.dfsek.terra.api.inventory.ItemStack;


/**
 * Loot LootFunction fot setting the amount of an item.
 */
public class AmountFunction implements LootFunction {
    private final int max;
    private final int min;
    
    /**
     * Instantiates an AmountFunction.
     *
     * @param min Minimum amount.
     * @param max Maximum amount.
     */
    public AmountFunction(int min, int max) {
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
        original.setAmount(r.nextInt(max - min + 1) + min);
        return original;
    }
}
