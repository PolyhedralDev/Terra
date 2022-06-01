/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.structures.loot;

import net.jafama.FastMath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dfsek.terra.addons.structure.structures.loot.functions.AmountFunction;
import com.dfsek.terra.addons.structure.structures.loot.functions.DamageFunction;
import com.dfsek.terra.addons.structure.structures.loot.functions.EnchantFunction;
import com.dfsek.terra.addons.structure.structures.loot.functions.LootFunction;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.ItemStack;


/**
 * Representation of a single item entry within a Loot Table pool.
 */
public class Entry {
    private final Item item;
    private final long weight;
    private final List<LootFunction> functions = new ArrayList<>();
    
    /**
     * Instantiates an Entry from a JSON representation.
     *
     * @param entry The JSON Object to instantiate from.
     */
    public Entry(JSONObject entry, Platform platform) {
        String id = entry.get("name").toString();
        this.item = platform.getItemHandle().createItem(id);
        
        long weight1;
        try {
            weight1 = (long) entry.get("weight");
        } catch(NullPointerException e) {
            weight1 = 1;
        }
        
        this.weight = weight1;
        if(entry.containsKey("functions")) {
            for(Object function : (JSONArray) entry.get("functions")) {
                switch(((String) ((JSONObject) function).get("function"))) {
                    case "minecraft:set_count", "set_count" -> {
                        Object loot = ((JSONObject) function).get("count");
                        long max, min;
                        if(loot instanceof Long) {
                            max = (Long) loot;
                            min = (Long) loot;
                        } else {
                            max = (long) ((JSONObject) loot).get("max");
                            min = (long) ((JSONObject) loot).get("min");
                        }
                        functions.add(new AmountFunction(FastMath.toIntExact(min), FastMath.toIntExact(max)));
                    }
                    case "minecraft:set_damage", "set_damage" -> {
                        long maxDamage = (long) ((JSONObject) ((JSONObject) function).get("damage")).get("max");
                        long minDamage = (long) ((JSONObject) ((JSONObject) function).get("damage")).get("min");
                        functions.add(new DamageFunction(FastMath.toIntExact(minDamage), FastMath.toIntExact(maxDamage)));
                    }
                    case "minecraft:enchant_with_levels", "enchant_with_levels" -> {
                        long maxEnchant = (long) ((JSONObject) ((JSONObject) function).get("levels")).get("max");
                        long minEnchant = (long) ((JSONObject) ((JSONObject) function).get("levels")).get("min");
                        JSONArray disabled = null;
                        if(((JSONObject) function).containsKey("disabled_enchants"))
                            disabled = (JSONArray) ((JSONObject) function).get("disabled_enchants");
                        functions.add(
                                new EnchantFunction(FastMath.toIntExact(minEnchant), FastMath.toIntExact(maxEnchant), disabled, platform));
                    }
                }
            }
        }
    }
    
    /**
     * Fetches a single ItemStack from the Entry, applying all functions to it.
     *
     * @param r The Random instance to apply functions with
     *
     * @return ItemStack - The ItemStack with all functions applied.
     */
    public ItemStack getItem(Random r) {
        ItemStack item = this.item.newItemStack(1);
        for(LootFunction f : functions) {
            item = f.apply(item, r);
        }
        return item;
    }
    
    /**
     * Gets the weight attribute of the Entry.
     *
     * @return long - The weight of the Entry.
     */
    public long getWeight() {
        return this.weight;
    }
}
