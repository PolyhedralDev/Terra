package com.dfsek.terra.api.gaea.tree.fractal;

import com.dfsek.terra.api.generic.world.vector.Location;

import java.util.Random;

public interface TreeGetter {
    FractalTree getTree(Location l, Random r);
}
