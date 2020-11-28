package com.dfsek.terra.generation.items.ores;

import org.polydev.gaea.math.Range;

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
