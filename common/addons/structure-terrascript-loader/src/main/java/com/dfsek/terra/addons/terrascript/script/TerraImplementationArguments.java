/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;


public class TerraImplementationArguments implements ImplementationArguments {
    private final Rotation rotation;
    private final Random random;
    private final WritableWorld world;
    private final Map<Vector3, String> marks = new HashMap<>();
    private final int recursions;
    private final Vector3Int origin;
    private boolean waterlog = false;
    
    public TerraImplementationArguments(Vector3Int origin, Rotation rotation, Random random, WritableWorld world, int recursions) {
        this.rotation = rotation;
        this.random = random;
        this.world = world;
        this.recursions = recursions;
        this.origin = origin;
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
    
    public WritableWorld getWorld() {
        return world;
    }
    
    public Vector3Int getOrigin() {
        return origin;
    }
    
    public void setMark(Vector3 pos, String mark) {
        marks.put(pos, mark);
    }
    
    public String getMark(Vector3 pos) {
        return marks.get(pos);
    }
}
