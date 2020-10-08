package com.dfsek.terra.biome.failsafe;

import org.bukkit.Bukkit;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;

/**
 * What happens if terrain generation is attempted with an unrecoverable config error.
 */
public enum FailType {
    /**
     * Return failover biome, then shut down server to minimize damage.
     * Generally the safest option.
     */
    SHUTDOWN {
        @Override
        public Biome fail() {
            Bukkit.getServer().shutdown();
            try {
                return new FailoverBiome();
            } catch(ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    },
    /**
     * Returns null, hard crashing the server, but not generating any corrupted terrain.<br>
     *     This option is <br>NOT</br> stable, but it has the least risk of blank chunks being generated.
     *     However, it has the highest risk of corruption!
     */
    CRASH {
        @Override
        public Biome fail() {
            return null;
        }
    },
    /**
     * Returns a failover biome, which generates completely blank chunks.
     * Recommended for debugging.
     */
    FAILOVER {
        @Override
        public Biome fail() {
            try {
                return new FailoverBiome();
            } catch(ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    /**
     * Performs the action specified by the enum type to occur on failure of terrain generation.
     * @return Failover biome, if specified, null if not.
     */
    public abstract Biome fail();
}
