package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.vector.Vector2Impl;
import com.dfsek.terra.vector.Vector3Impl;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedStateManipulator;
import com.dfsek.terra.api.structures.tokenizer.Position;
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
        Vector2 xz = new Vector2Impl(x.apply(implementationArguments, variableMap).doubleValue(), z.apply(implementationArguments, variableMap).doubleValue());
        RotationUtil.rotateVector(xz, arguments.getRotation());

        arguments.getBuffer().addItem(new BufferedStateManipulator(main, data.apply(implementationArguments, variableMap)), new Vector3Impl(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).intValue(), FastMath.roundToInt(xz.getZ())).toLocation(arguments.getBuffer().getOrigin().getWorld()));
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
