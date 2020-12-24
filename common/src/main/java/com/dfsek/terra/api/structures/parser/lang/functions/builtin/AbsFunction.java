package com.dfsek.terra.api.structures.parser.lang.functions.builtin;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

public class AbsFunction extends MathFunction {
    private final Returnable<Number> returnable;

    public AbsFunction(Position position, Returnable<Number> returnable) {
        super(position);
        this.returnable = returnable;
    }

    @Override
    public String name() {
        return "abs";
    }

    @Override
    public Number apply(Buffer buffer, Rotation rotation, int recursions) {
        return FastMath.abs(returnable.apply(buffer, rotation, recursions).doubleValue());
    }
}
