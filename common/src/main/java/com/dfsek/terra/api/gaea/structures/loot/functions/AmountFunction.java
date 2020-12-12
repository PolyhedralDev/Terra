package com.dfsek.terra.api.gaea.structures.loot.functions;


import com.dfsek.terra.api.generic.inventory.ItemStack;

import java.util.Random;

/**
 * Loot Function fot setting the amount of an item.
 */
public class AmountFunction implements Function {
    private final int max;
    private final int min;

    /**
     * Instantiates an AmountFunction.
     *
     * @param min Minimum amount.
     * @param max Maximum amount.
     */
    public AmountFunction(int min, int max) {
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
        original.setAmount(r.nextInt(max - min + 1) + min);
        return original;
    }
}
