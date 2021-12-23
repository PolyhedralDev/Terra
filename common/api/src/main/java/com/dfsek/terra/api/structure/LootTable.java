/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure;

import org.jetbrains.annotations.ApiStatus.Experimental;

import java.util.List;
import java.util.Random;

import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.api.inventory.ItemStack;


@Experimental
public interface LootTable {
    /**
     * Fills an Inventory with loot.
     *
     * @param i The Inventory to fill.
     * @param r The The Random instance to use.
     */
    void fillInventory(Inventory i, Random r);
    
    /**
     * Fetches a list of ItemStacks from the loot table using the given Random instance.
     *
     * @param r The Random instance to use.
     *
     * @return List&lt;ItemStack&gt; - The list of loot fetched.
     */
    List<ItemStack> getLoot(Random r);
}
