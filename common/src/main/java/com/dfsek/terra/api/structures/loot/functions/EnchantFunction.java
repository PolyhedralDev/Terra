package com.dfsek.terra.api.structures.loot.functions;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.api.util.GlueList;
import net.jafama.FastMath;
import org.json.simple.JSONArray;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EnchantFunction implements LootFunction {
    private final int min;
    private final int max;
    private final JSONArray disabled;
    private final TerraPlugin main;


    public EnchantFunction(int min, int max, JSONArray disabled, TerraPlugin main) {
        this.max = max;
        this.min = min;
        this.disabled = disabled;
        this.main = main;
    }

    /**
     * Applies the function to an ItemStack.
     *
     * @param original The ItemStack on which to apply the function.
     * @param r        The Random instance to use.
     * @return - ItemStack - The mutated ItemStack.
     */
    @Override
    public ItemStack apply(ItemStack original, Random r) {
        double enchant = (r.nextDouble() * (max - min)) + min;
        List<Enchantment> possible = new GlueList<>();
        for(Enchantment ench : main.getItemHandle().getEnchantments()) {
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
                main.logger().warning("Attempted to enchant " + original.getType() + " with " + chosen + " at level " + FastMath.max(lvl, 1) + ", but an unexpected exception occurred! Usually this is caused by a misbehaving enchantment plugin.");
            }
        }
        original.setItemMeta(meta);
        return original;
    }
}
