package com.dfsek.terra.api.structure;

import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.api.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public interface LootTable {
    /**
     * Fetches a list of ItemStacks from the loot table using the given Random instance.
     *
     * @param r The Random instance to use.
     * @return List&lt;ItemStack&gt; - The list of loot fetched.
     */
    List<ItemStack> getLoot(Random r);

    /**
     * Fills an Inventory with loot.
     *
     * @param i The Inventory to fill.
     * @param r The The Random instance to use.
     */
    void fillInventory(Inventory i, Random r);
}
