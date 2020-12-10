package com.dfsek.terra.api.gaea.tree;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.Set;

public interface Tree {
    boolean plant(Location l, Random r, JavaPlugin main);

    Set<Material> getSpawnable();
}
