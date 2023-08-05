/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.functions;

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
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.event.events.world.generation.EntitySpawnEvent;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class EntityFunction implements Function<Void> {
    private final EntityType data;
    private final Expression<Number> x, y, z;
    private final SourcePosition position;
    private final Platform platform;
    
    public EntityFunction(Expression<Number> x, Expression<Number> y, Expression<Number> z, Expression<String> data, Platform platform,
                          SourcePosition position) {
        this.position = position;
        this.platform = platform;
        if(!(data instanceof ConstantExpression)) throw new ParseException("Entity data must be constant", data.getPosition());
        
        this.data = platform.getWorldHandle().getEntity(((ConstantExpression<String>) data).getConstant());
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
        
        Entity entity = arguments.getWorld().spawnEntity(Vector3.of(xz.getX(), y.evaluate(implementationArguments, scope).doubleValue(),
                                                                    xz.getZ())
                                                                .mutable()
                                                                .add(arguments.getOrigin())
                                                                .add(0.5, 0, 0.5)
                                                                .immutable(), data);
        platform.getEventManager().callEvent(new EntitySpawnEvent(entity.world().getPack(), entity));
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
