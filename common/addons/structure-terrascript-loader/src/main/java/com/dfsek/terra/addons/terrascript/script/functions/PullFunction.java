/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import net.jafama.FastMath;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.ConstantExpression;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class PullFunction implements Function<Void> {
    private final BlockState data;
    private final Returnable<Number> x, y, z;
    private final Position position;
    
    public PullFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, Platform platform,
                        Position position) {
        this.position = position;
        if(!(data instanceof ConstantExpression)) throw new ParseException("Block data must be constant", data.getPosition());
        
        this.data = platform.getWorldHandle().createBlockState(((ConstantExpression<String>) data).getConstant());
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public Void apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = RotationUtil.rotateVector(Vector2.of(x.apply(implementationArguments, scope).doubleValue(),
                                                          z.apply(implementationArguments, scope).doubleValue()), arguments.getRotation());
        
        Vector3.Mutable mutable = Vector3.of(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, scope).intValue(),
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
    public Position getPosition() {
        return position;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
