/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import net.jafama.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class StateFunction implements Function<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateFunction.class);
    private final Returnable<String> data;
    private final Returnable<Number> x, y, z;
    private final Position position;
    
    public StateFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data,
                         Position position) {
        this.position = position;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public Void apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = RotationUtil.rotateVector(Vector2.of(x.apply(implementationArguments, scope).doubleValue(),
                                                          z.apply(implementationArguments, scope).doubleValue()), arguments.getRotation());
        
        
        Vector3 origin = Vector3.of(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, scope).intValue(),
                                    FastMath.roundToInt(xz.getZ())).mutable().add(arguments.getOrigin()).immutable();
        try {
            BlockEntity state = arguments.getWorld().getBlockEntity(origin);
            state.applyState(data.apply(implementationArguments, scope));
            state.update(false);
        } catch(Exception e) {
            LOGGER.warn("Could not apply BlockState at {}", origin, e);
            e.printStackTrace();
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
