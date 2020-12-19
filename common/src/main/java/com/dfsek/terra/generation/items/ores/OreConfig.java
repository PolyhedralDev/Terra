package com.dfsek.terra.generation.items.ores;

import com.dfsek.terra.api.math.Range;

public class OreConfig {
    private final Range amount;
    private final Range height;

    public OreConfig(Range amount, Range height) {
        this.amount = amount;
        this.height = height;
    }

    public Range getAmount() {
        return amount;
    }

    public Range getHeight() {
        return height;
    }
}
