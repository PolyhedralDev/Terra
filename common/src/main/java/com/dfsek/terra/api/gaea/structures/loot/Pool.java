package com.dfsek.terra.api.gaea.structures.loot;

import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import net.jafama.FastMath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Random;

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
    public Pool(JSONObject pool, TerraPlugin main) {
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
            Entry entry = new Entry((JSONObject) entryJSON, main);
            entries.add(entry, FastMath.toIntExact(entry.getWeight()));
        }
    }

    /**
     * Fetches a list of items from the pool using the provided Random instance.
     *
     * @param r The Random instance to use.
     * @return List&lt;ItemStack&gt; - The list of items fetched.
     */
    public List<ItemStack> getItems(Random r) {

        int rolls = r.nextInt(max - min + 1) + min;
        List<ItemStack> items = new GlueList<>();
        for(int i = 0; i < rolls; i++) {
            items.add(entries.get(r).getItem(r));
        }
        return items;
    }
}
