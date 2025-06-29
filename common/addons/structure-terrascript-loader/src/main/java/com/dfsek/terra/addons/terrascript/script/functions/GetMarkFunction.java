/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.seismic.math.floatingpoint.FloatingPointFunctions;
import com.dfsek.seismic.type.vector.Vector2;
import com.dfsek.seismic.type.vector.Vector3;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


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
    public String apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = Vector2.Mutable.of(x.apply(implementationArguments, scope).doubleValue(),
            z.apply(implementationArguments, scope).doubleValue()).rotate(arguments.getRotation());

        String mark = arguments.getMark(Vector3.of(FloatingPointFunctions.floor(xz.getX()), FloatingPointFunctions.floor(
                    y.apply(implementationArguments, scope).doubleValue()),
                FloatingPointFunctions.floor(xz.getZ()))
            .mutable()
            .add(arguments.getOrigin().toFloat())
            .immutable());
        return mark == null ? "" : mark;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
}
