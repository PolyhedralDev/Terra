/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.structures.loot.functions;


import java.util.random.RandomGenerator;

import com.dfsek.terra.api.inventory.ItemStack;


/**
 * Interface for mutating items in Loot Tables.
 */
public interface LootFunction {
    /**
     * Applies the function to an ItemStack.
     *
     * @param original The ItemStack on which to apply the function.
     * @param r        The RandomGenerator instance to use.
     *
     * @return - ItemStack - The mutated ItemStack.
     */
    ItemStack apply(ItemStack original, RandomGenerator r);
}
