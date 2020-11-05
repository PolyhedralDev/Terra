package com.dfsek.terra.structure.features;

import com.dfsek.terra.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.Random;

public interface Feature {
    void apply(Structure structure, Location l, Chunk chunk);
    void apply(Structure structure, Location l, Random random);
}
