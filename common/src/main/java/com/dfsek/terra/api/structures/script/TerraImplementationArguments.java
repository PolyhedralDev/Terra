package com.dfsek.terra.api.structures.script;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;

import java.util.Random;

public class TerraImplementationArguments implements ImplementationArguments {
    private final Buffer buffer;
    private final Rotation rotation;
    private final Random random;
    private final int recursions;

    public TerraImplementationArguments(Buffer buffer, Rotation rotation, Random random, int recursions) {
        this.buffer = buffer;
        this.rotation = rotation;
        this.random = random;
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
}
