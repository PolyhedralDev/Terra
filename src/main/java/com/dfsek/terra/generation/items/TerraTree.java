package com.dfsek.terra.generation.items;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.tree.Tree;

import java.util.Random;

public class TerraTree implements Tree {
    @Override
    public boolean plant(Location location, Random random, JavaPlugin javaPlugin) {
        return false;
    }
}
