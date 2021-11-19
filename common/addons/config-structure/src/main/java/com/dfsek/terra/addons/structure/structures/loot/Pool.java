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

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


/**
 * Representation of a Loot Table pool, or a set of items to be fetched independently.
 */
public class Pool {
    private final int max;
    private final int min;
    private final ProbabilityCollection<Entry> entries;
    
    /**
     * Instantiates a Pool from a JSON representation.
     *
     * @param pool The JSON Object to instantiate from.
     */
    public Pool(JSONObject pool, Platform platform) {
        entries = new ProbabilityCollection<>();
        Object amount = pool.get("rolls");
        if(amount instanceof Long) {
            max = FastMath.toIntExact((Long) amount);
            min = FastMath.toIntExact((Long) amount);
        } else {
            max = FastMath.toIntExact((Long) ((JSONObject) amount).get("max"));
            min = FastMath.toIntExact((Long) ((JSONObject) amount).get("min"));
        }
        
        for(Object entryJSON : (JSONArray) pool.get("entries")) {
            Entry entry = new Entry((JSONObject) entryJSON, platform);
            entries.add(entry, FastMath.toIntExact(entry.getWeight()));
        }
    }
    
    /**
     * Fetches a list of items from the pool using the provided Random instance.
     *
     * @param r The Random instance to use.
     *
     * @return List&lt;ItemStack&gt; - The list of items fetched.
     */
    public List<ItemStack> getItems(Random r) {
        
        int rolls = r.nextInt(max - min + 1) + min;
        List<ItemStack> items = new ArrayList<>();
        for(int i = 0; i < rolls; i++) {
            items.add(entries.get(r).getItem(r));
        }
        return items;
    }
}
