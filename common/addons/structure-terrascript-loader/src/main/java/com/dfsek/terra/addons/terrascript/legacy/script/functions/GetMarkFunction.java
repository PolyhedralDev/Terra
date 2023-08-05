/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.functions;

import net.jafama.FastMath;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class GetMarkFunction implements Function<String> {
    private final Expression<Number> x, y, z;
    private final SourcePosition position;
    
    public GetMarkFunction(Expression<Number> x, Expression<Number> y, Expression<Number> z, SourcePosition position) {
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String evaluate(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = RotationUtil.rotateVector(Vector2.of(x.evaluate(implementationArguments, scope).doubleValue(),
                                                          z.evaluate(implementationArguments, scope).doubleValue()),
                                               arguments.getRotation());
        
        String mark = arguments.getMark(Vector3.of(FastMath.floorToInt(xz.getX()), FastMath.floorToInt(
                                                           y.evaluate(implementationArguments, scope).doubleValue()),
                                                   FastMath.floorToInt(xz.getZ()))
                                               .mutable()
                                               .add(arguments.getOrigin())
                                               .immutable());
        return mark == null ? "" : mark;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    @Override
    public Type returnType() {
        return Type.STRING;
    }
}
