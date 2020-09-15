package com.dfsek.terra;

import com.dfsek.terra.config.BiomeConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.polydev.gaea.world.carving.Carver;
import org.polydev.gaea.world.carving.Worm;

import java.util.Random;

public class UserDefinedCarver extends Carver {
    private final int chance;
    private final int minLength;
    private final int maxLength;
    public UserDefinedCarver(ConfigurationSection config) {
        super(config.getInt("y.min"), config.getInt("y.max"));
        this.chance = config.getInt("chance");
        minLength = config.getInt("length.min");
        maxLength = config.getInt("length.max");

    }

    @Override
    public Worm getWorm(long l, Vector vector) {
        return null;
    }

    @Override
    public boolean isChunkCarved(Random random) {
        return random.nextInt(100) < chance;
    }

    private static class UserDefinedWorm extends Worm {

        public UserDefinedWorm(int length, Random r, Vector origin) {
            super(length, r, origin);
        }

        @Override
        public void step() {

        }
    }
}
