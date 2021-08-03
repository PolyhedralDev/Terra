package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.terra.addons.terrascript.buffer.items.BufferedStateManipulator;
import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.api.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;
import net.jafama.FastMath;

import java.util.Map;

public class StateFunction implements Function<Void> {
    private final Returnable<String> data;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final TerraPlugin main;

    public StateFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, TerraPlugin main, Position position) {
        this.position = position;
        this.main = main;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Void apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = new Vector2(x.apply(implementationArguments, variableMap).doubleValue(), z.apply(implementationArguments, variableMap).doubleValue());
        RotationUtil.rotateVector(xz, arguments.getRotation());

        arguments.getBuffer().addItem(new BufferedStateManipulator(main, data.apply(implementationArguments, variableMap)), new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).intValue(), FastMath.roundToInt(xz.getZ())));
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
