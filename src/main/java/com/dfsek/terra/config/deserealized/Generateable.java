package com.dfsek.terra.config.deserealized;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

@SuppressWarnings("unused")
public interface Generateable {
    void generate(Location location, Random random, JavaPlugin plugin);

    boolean isValidLocation(Location location, JavaPlugin plugin);
}
