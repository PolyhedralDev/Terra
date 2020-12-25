package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.structure.buffer.items.Mark;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

public class GetMarkFunction implements Function<String> {
    private final Returnable<Number> x, y, z;
    private final Position position;

    public GetMarkFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) throws ParseException {
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String name() {
        return "block";
    }

    @Override
    public String apply(Buffer buffer, Rotation rotation, int recursions) {
        Vector2 xz = new Vector2(x.apply(buffer, rotation, recursions).doubleValue(), z.apply(buffer, rotation, recursions).doubleValue());

        RotationUtil.rotateVector(xz, rotation);
        Mark mark = buffer.getMark(new Vector3(FastMath.roundToInt(xz.getX()), y.apply(buffer, rotation, recursions).intValue(), FastMath.roundToInt(xz.getZ())));
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
