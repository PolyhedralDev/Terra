package com.dfsek.terra.api.structures.parser.lang.functions.builtin;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

public class SqrtFunction extends MathFunction {
    private final Returnable<Number> returnable;

    public SqrtFunction(Position position, Returnable<Number> returnable) {
        super(position);
        this.returnable = returnable;
    }

    @Override
    public String name() {
        return "sqrt";
    }

    @Override
    public Number apply(Location location, Rotation rotation, int recursions) {
        return FastMath.sqrt(returnable.apply(location, rotation, recursions).doubleValue());
    }

    @Override
    public Number apply(Location location, Chunk chunk, Rotation rotation, int recursions) {
        return FastMath.sqrt(returnable.apply(location, chunk, rotation, recursions).doubleValue());
    }
}
