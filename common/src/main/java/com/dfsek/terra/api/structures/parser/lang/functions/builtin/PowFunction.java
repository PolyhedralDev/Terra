package com.dfsek.terra.api.structures.parser.lang.functions.builtin;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

public class PowFunction extends MathFunction {
    private final Returnable<Number> base;
    private final Returnable<Number> power;

    public PowFunction(Position position, Returnable<Number> base, Returnable<Number> power) {
        super(position);
        this.base = base;
        this.power = power;
    }

    @Override
    public String name() {
        return "pow";
    }

    @Override
    public Number apply(Buffer buffer, Rotation rotation, int recursions) {
        return FastMath.pow(base.apply(buffer, rotation, recursions).doubleValue(), power.apply(buffer, rotation, recursions).doubleValue());
    }
}
