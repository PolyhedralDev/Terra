package com.dfsek.terra.api.gaea.tree;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Random;
import java.util.Set;

public interface Tree {
    boolean plant(Location l, Random r);

    Set<Material> getSpawnable();
}
