package com.dfsek.terra.biome.failsafe;

import com.dfsek.terra.biome.UserDefinedBiome;
import parsii.tokenizer.ParseException;

/**
 * Blank biome to generate in case of a severe config error
 */
public final class FailoverBiome extends UserDefinedBiome {
    public FailoverBiome() throws ParseException {
        super(org.bukkit.block.Biome.PLAINS, new FailoverDecorator(), new FailoverGenerator(), false, "FAILSAFE");
    }
}
