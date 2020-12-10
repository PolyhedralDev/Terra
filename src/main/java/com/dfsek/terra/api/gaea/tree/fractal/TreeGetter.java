package com.dfsek.terra.api.gaea.tree.fractal;

import org.bukkit.Location;

import java.util.Random;

public interface TreeGetter {
    FractalTree getTree(Location l, Random r);
}
