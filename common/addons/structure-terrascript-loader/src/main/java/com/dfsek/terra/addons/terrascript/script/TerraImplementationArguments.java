/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script;

import java.util.Random;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.world.access.World;
import com.dfsek.terra.api.world.access.WorldAccess;


public class TerraImplementationArguments implements ImplementationArguments {
    private final Buffer buffer;
    private final Rotation rotation;
    private final Random random;
    private final WorldAccess world;
    private final int recursions;
    private boolean waterlog = false;
    
    public TerraImplementationArguments(Buffer buffer, Rotation rotation, Random random, WorldAccess world, int recursions) {
        this.buffer = buffer;
        this.rotation = rotation;
        this.random = random;
        this.world = world;
        this.recursions = recursions;
    }
    
    public Buffer getBuffer() {
        return buffer;
    }
    
    public int getRecursions() {
        return recursions;
    }
    
    public Random getRandom() {
        return random;
    }
    
    public Rotation getRotation() {
        return rotation;
    }
    
    public boolean isWaterlog() {
        return waterlog;
    }
    
    public void setWaterlog(boolean waterlog) {
        this.waterlog = waterlog;
    }
    
    public WorldAccess getWorld() {
        return world;
    }
}
