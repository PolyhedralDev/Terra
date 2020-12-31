package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Random;

public class LootFunction implements Function<Void> {
    private final Returnable<String> data;
    private final Returnable<Number> x, y, z;
    private final Position position;

    public LootFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, TerraPlugin main, Position position) {
        this.position = position;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String name() {
        return "block";
    }

    @Override
    public Void apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        Vector2 xz = new Vector2(x.apply(buffer, rotation, random, recursions).doubleValue(), z.apply(buffer, rotation, random, recursions).doubleValue());


        return null;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
