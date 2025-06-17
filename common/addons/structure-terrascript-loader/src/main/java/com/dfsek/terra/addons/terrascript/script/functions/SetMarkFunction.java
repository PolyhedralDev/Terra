/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.seismic.math.floatingpoint.FloatingPointFunctions;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.seismic.type.vector.Vector2;
import com.dfsek.seismic.type.vector.Vector3;


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
    public Void apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = Vector2.Mutable.of(x.apply(implementationArguments, scope).doubleValue(),
            z.apply(implementationArguments, scope).doubleValue()).rotate(arguments.getRotation());


        arguments.setMark(Vector3.of(FloatingPointFunctions.floor(xz.getX()),
                FloatingPointFunctions.floor(
                    y.apply(implementationArguments, scope).doubleValue()),
                FloatingPointFunctions.floor(xz.getZ())).mutable().add(arguments.getOrigin().toFloat()).immutable(),
            mark.apply(implementationArguments, scope));
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
