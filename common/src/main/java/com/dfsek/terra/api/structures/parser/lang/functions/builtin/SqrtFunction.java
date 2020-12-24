package com.dfsek.terra.api.structures.parser.lang.functions.builtin;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
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
    public Number apply(Buffer buffer, Rotation rotation, int recursions) {
        return FastMath.sqrt(returnable.apply(buffer, rotation, recursions).doubleValue());
    }
}
