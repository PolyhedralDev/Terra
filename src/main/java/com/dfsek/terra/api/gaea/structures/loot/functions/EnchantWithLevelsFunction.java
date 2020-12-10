package com.dfsek.terra.api.gaea.structures.loot.functions;

import com.dfsek.terra.api.gaea.util.GlueList;
import net.jafama.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EnchantWithLevelsFunction implements Function {
    private final int min;
    private final int max;
    private final JSONArray disabled;


    public EnchantWithLevelsFunction(int min, int max, JSONArray disabled) {
        this.max = max;
        this.min = min;
        this.disabled = disabled;
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
        for(Enchantment ench : Enchantment.values()) {
            if(ench.canEnchantItem(original) && (disabled == null || !this.disabled.contains(ench.getName()))) {
                possible.add(ench);
            }
        }
        int numEnchant = (r.nextInt((int) FastMath.abs(enchant)) / 10 + 1);
        if(possible.size() >= numEnchant) {
            Collections.shuffle(possible);
            iter:
            for(int i = 0; i < numEnchant; i++) {
                Enchantment chosen = possible.get(i);
                for(Enchantment ench : original.getEnchantments().keySet()) {
                    if(chosen.conflictsWith(ench)) continue iter;
                }
                int lvl = r.nextInt(1 + (int) (((enchant / 40 > 1) ? 1 : enchant / 40) * (chosen.getMaxLevel())));
                try {
                    original.addEnchantment(chosen, FastMath.max(lvl, 1));
                } catch(IllegalArgumentException e) {
                    Bukkit.getLogger().warning("[Gaea] Attempted to enchant " + original.getType() + " with " + chosen + " at level " + FastMath.max(lvl, 1) + ", but an unexpected exception occurred! Usually this is caused by a misbehaving enchantment plugin.");
                }
            }
        }
        return original;
    }
}
