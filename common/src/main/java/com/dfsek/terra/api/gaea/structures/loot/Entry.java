package com.dfsek.terra.api.gaea.structures.loot;

import com.dfsek.terra.api.gaea.structures.loot.functions.AmountFunction;
import com.dfsek.terra.api.gaea.structures.loot.functions.DamageFunction;
import com.dfsek.terra.api.gaea.structures.loot.functions.Function;
import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.inventory.ItemStack;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import net.jafama.FastMath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Random;

/**
 * Representation of a single item entry within a Loot Table pool.
 */
public class Entry {
    private final MaterialData item;
    private final long weight;
    private final List<Function> functions = new GlueList<>();
    private final TerraPlugin main;

    /**
     * Instantiates an Entry from a JSON representation.
     *
     * @param entry The JSON Object to instantiate from.
     */
    public Entry(JSONObject entry, TerraPlugin main) {
        this.main = main;
        String id = entry.get("name").toString();
        this.item = main.getWorldHandle().createMaterialData(id);

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
                    case "minecraft:set_count":
                    case "set_count":
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
                        break;
                    case "minecraft:set_damage":
                    case "set_damage":
                        long maxDamage = (long) ((JSONObject) ((JSONObject) function).get("damage")).get("max");
                        long minDamage = (long) ((JSONObject) ((JSONObject) function).get("damage")).get("min");
                        functions.add(new DamageFunction(FastMath.toIntExact(minDamage), FastMath.toIntExact(maxDamage)));
                        break;
                }
            }
        }
    }

    /**
     * Fetches a single ItemStack from the Entry, applying all functions to it.
     *
     * @param r The Random instance to apply functions with
     * @return ItemStack - The ItemStack with all functions applied.
     */
    public ItemStack getItem(Random r) {
        ItemStack item = main.getItemHandle().newItemStack(this.item, 1);
        for(Function f : functions) {
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
