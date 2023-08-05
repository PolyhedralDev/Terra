/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.functions;

import net.jafama.FastMath;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.constants.ConstantExpression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class PullFunction implements Function<Void> {
    private final BlockState data;
    private final Expression<Number> x, y, z;
    private final SourcePosition position;
    
    public PullFunction(Expression<Number> x, Expression<Number> y, Expression<Number> z, Expression<String> data, Platform platform,
                        SourcePosition position) {
        this.position = position;
        if(!(data instanceof ConstantExpression)) throw new ParseException("Block data must be constant", data.getPosition());
        
        this.data = platform.getWorldHandle().createBlockState(((ConstantExpression<String>) data).getConstant());
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public Void evaluate(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = RotationUtil.rotateVector(Vector2.of(x.evaluate(implementationArguments, scope).doubleValue(),
                                                          z.evaluate(implementationArguments, scope).doubleValue()),
                                               arguments.getRotation());
        
        Vector3.Mutable mutable = Vector3.of(FastMath.roundToInt(xz.getX()), y.evaluate(implementationArguments, scope).intValue(),
                                             FastMath.roundToInt(xz.getZ())).mutable().add(arguments.getOrigin());
        while(mutable.getY() > arguments.getWorld().getMinHeight()) {
            if(!arguments.getWorld().getBlockState(mutable).isAir()) {
                arguments.getWorld().setBlockState(mutable, data);
                break;
            }
            mutable.subtract(0, 1, 0);
        }
        return null;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    @Override
    public Type returnType() {
        return Type.VOID;
    }
}
