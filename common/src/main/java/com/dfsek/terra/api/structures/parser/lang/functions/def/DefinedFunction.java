package com.dfsek.terra.api.structures.parser.lang.functions.def;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Random;

public abstract class DefinedFunction<T> implements Function<T> {
    private final Block block;
    private final String name;

    protected DefinedFunction(Block block, String name) {
        this.block = block;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public T apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        return null;
    }

    @Override
    public Position getPosition() {
        return null;
    }
}
