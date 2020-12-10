package com.dfsek.terra.api.gaea.structures.loot.functions;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

/**
 * Loot Function for setting the damage on items in Loot Tables
 */
public class DamageFunction implements Function {
    private final int max;
    private final int min;

    /**
     * Instantiates a DamageFunction.
     *
     * @param min Minimum amount of damage (percentage, out of 100)
     * @param max Maximum amount of damage (percentage, out of 100)
     */
    public DamageFunction(int min, int max) {
        this.min = min;
        this.max = max;
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
        double itemDurability = (r.nextDouble() * (max - min)) + min;
        Damageable damage = (Damageable) original.getItemMeta();
        damage.setDamage((int) (original.getType().getMaxDurability() - (itemDurability / 100) * original.getType().getMaxDurability()));
        original.setItemMeta((ItemMeta) damage);
        return original;
    }
}
