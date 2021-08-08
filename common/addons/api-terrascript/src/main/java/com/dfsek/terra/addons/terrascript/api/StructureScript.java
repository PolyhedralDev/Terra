package com.dfsek.terra.addons.terrascript.api;

import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;

import java.util.Random;

public interface StructureScript extends Structure {
    @SuppressWarnings("try")
    boolean test(Vector3 location, World world, Random random, Rotation rotation);
}
