package com.dfsek.terra.structure.features;

import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.Random;

public interface Feature {
    void apply(Structure structure, Rotation r, Location l, Chunk chunk);

    void apply(Structure structure, Rotation r, Location l, Random random);
}
