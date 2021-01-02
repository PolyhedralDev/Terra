package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.items.Mark;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

public class GetMarkFunction implements Function<String> {
    private final Returnable<Number> x, y, z;
    private final Position position;

    public GetMarkFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) {
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String apply(ImplementationArguments implementationArguments) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = new Vector2(x.apply(implementationArguments).doubleValue(), z.apply(implementationArguments).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());
        Mark mark = arguments.getBuffer().getMark(new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments).intValue(), FastMath.roundToInt(xz.getZ())));
        return mark == null ? "" : mark.getContent();
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
