/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.functions;

import net.jafama.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class StateFunction implements Function<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateFunction.class);
    private final Expression<String> data;
    private final Expression<Number> x, y, z;
    private final SourcePosition position;
    
    public StateFunction(Expression<Number> x, Expression<Number> y, Expression<Number> z, Expression<String> data,
                         SourcePosition position) {
        this.position = position;
        this.data = data;
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
        
        
        Vector3 origin = Vector3.of(FastMath.roundToInt(xz.getX()), y.evaluate(implementationArguments, scope).intValue(),
                                    FastMath.roundToInt(xz.getZ())).mutable().add(arguments.getOrigin()).immutable();
        try {
            BlockEntity state = arguments.getWorld().getBlockEntity(origin);
            state.applyState(data.evaluate(implementationArguments, scope));
            state.update(false);
        } catch(Exception e) {
            LOGGER.warn("Could not apply BlockState at {}", origin, e);
            e.printStackTrace();
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
