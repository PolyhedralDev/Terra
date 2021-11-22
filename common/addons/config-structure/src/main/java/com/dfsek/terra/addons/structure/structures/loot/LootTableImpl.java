/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.structures.loot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.inventory.Inventory;
import com.dfsek.terra.api.inventory.ItemStack;


/**
 * Class representation of a Loot Table to populate chest loot.
 */
public class LootTableImpl implements com.dfsek.terra.api.structure.LootTable {
    private final List<Pool> pools = new ArrayList<>();
    
    /**
     * Instantiates a LootTable from a JSON String.
     *
     * @param json The JSON String representing the loot table.
     *
     * @throws ParseException if malformed JSON is passed.
     */
    public LootTableImpl(String json, Platform platform) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        Object tableJSON = jsonParser.parse(json);
        JSONArray poolArray = (JSONArray) ((JSONObject) tableJSON).get("pools");
        for(Object pool : poolArray) {
            pools.add(new Pool((JSONObject) pool, platform));
        }
    }
    
    @Override
    public void fillInventory(Inventory i, Random r) {
        List<ItemStack> loot = getLoot(r);
        for(ItemStack stack : loot) {
            int attempts = 0;
            while(stack.getAmount() != 0 && attempts < 10) {
                ItemStack newStack = stack.getType().newItemStack(stack.getAmount());
                newStack.setItemMeta(stack.getItemMeta());
                newStack.setAmount(1);
                int slot = r.nextInt(i.getSize());
                ItemStack slotItem = i.getItem(slot);
                if(slotItem == null) {
                    i.setItem(slot, newStack);
                    stack.setAmount(stack.getAmount() - 1);
                } else if(slotItem.getType().equals(newStack.getType())) {
                    ItemStack dep = newStack.getType().newItemStack(newStack.getAmount());
                    dep.setItemMeta(newStack.getItemMeta());
                    dep.setAmount(newStack.getAmount() + slotItem.getAmount());
                    i.setItem(slot, dep);
                    stack.setAmount(stack.getAmount() - 1);
                }
                attempts++;
            }
        }
    }
    
    @Override
    public List<ItemStack> getLoot(Random r) {
        List<ItemStack> itemList = new ArrayList<>();
        for(Pool pool : pools) {
            itemList.addAll(pool.getItems(r));
        }
        return itemList;
    }
}
