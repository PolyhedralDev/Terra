package com.dfsek.terra.generation.deserelized;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

@SuppressWarnings("unused")
public interface GenerationEntity {
    void generate(Location location, Random random, JavaPlugin plugin);

    boolean isValidLocation(Location location, JavaPlugin plugin);
}
