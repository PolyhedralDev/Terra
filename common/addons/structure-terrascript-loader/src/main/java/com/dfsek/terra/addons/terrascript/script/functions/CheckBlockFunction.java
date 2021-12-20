/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import net.jafama.FastMath;

import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class CheckBlockFunction implements Function<String> {
    private final Returnable<Number> x, y, z;
    private final Position position;
    
    public CheckBlockFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.position = position;
    }
    
    
    @Override
    public String apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        
        Vector2 xz = Vector2.of(x.apply(implementationArguments, variableMap).doubleValue(),
                                 z.apply(implementationArguments, variableMap).doubleValue());
        
        RotationUtil.rotateVector(xz, arguments.getRotation());
        
        String data = arguments.getWorld()
                               .getBlockState(arguments.getOrigin()
                                                       .toVector3()
                                                       .add(Vector3.of(FastMath.roundToInt(xz.getX()),
                                                                        y.apply(implementationArguments, variableMap)
                                                                         .doubleValue(), FastMath.roundToInt(xz.getZ()))))
                               .getAsString();
        if(data.contains("[")) return data.substring(0, data.indexOf('[')); // Strip properties
        else return data;
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
