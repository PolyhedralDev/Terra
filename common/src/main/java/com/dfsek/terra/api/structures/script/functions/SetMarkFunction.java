package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

public class SetMarkFunction implements Function<Void> {
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final Returnable<String> mark;

    public SetMarkFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> mark, Position position) {
        this.position = position;
        this.mark = mark;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Void apply(ImplementationArguments implementationArguments) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = new Vector2(x.apply(implementationArguments).doubleValue(), z.apply(implementationArguments).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        arguments.getBuffer().setMark(mark.apply(implementationArguments), new Vector3(FastMath.floorToInt(xz.getX()), FastMath.floorToInt(y.apply(implementationArguments).doubleValue()), FastMath.floorToInt(xz.getZ())).toLocation(arguments.getBuffer().getOrigin().getWorld()));
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
